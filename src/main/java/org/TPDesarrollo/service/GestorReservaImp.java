package org.TPDesarrollo.service;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.OcupanteDTO;
import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.TPDesarrollo.repository.EstadiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestorReservaImp implements GestorReserva {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HuespedRepository huespedRepository;
    private final EstadiaRepository estadiaRepository;

    @Override
    @Transactional
    public List<Reserva> crearReserva(ReservaDTO dto) {
        return procesarReserva(dto, "RESERVADA");
    }

    @Override
    @Transactional
    public List<Reserva> crearOcupacion(ReservaDTO dto) {
        return procesarReserva(dto, "OCUPADA");
    }

    // --- ELIMINAR POR ID DIRECTO (Si se tiene el ID) ---
    @Override
    @Transactional
    public void eliminarReserva(Integer id) throws Exception {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva no existe con ID: " + id));

        if (!"RESERVADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new RuntimeException("No se puede eliminar: La habitación ya está OCUPADA o finalizada.");
        }
        reservaRepository.delete(reserva);
    }

    // --- LÓGICA CORREGIDA PARA ELIMINAR DESDE LA GRILLA ---
    @Override
    @Transactional
    public void cancelarReservaPorFecha(Integer idHabitacion, LocalDate fecha) throws Exception {

        // CORRECCIÓN CRÍTICA: Usamos 'buscarReservaActivaEnFecha' en lugar de 'encontrarSolapamientos'.
        // 'encontrarSolapamientos' usa lógica estricta (< y >) para evitar choques, por lo que fallaba
        // al buscar una reserva que empieza exactamente en la fecha 'fecha'.
        // 'buscarReservaActivaEnFecha' usa lógica inclusiva (<=) para encontrarla correctamente.

        Reserva reservaAEliminar = reservaRepository.buscarReservaActivaEnFecha(idHabitacion, fecha)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna reserva PENDIENTE para borrar en esa fecha."));

        // La query del repositorio ya filtra que el estado sea 'RESERVADA', así que borramos directo.
        reservaRepository.delete(reservaAEliminar);
    }

    private List<Reserva> procesarReserva(ReservaDTO dto, String estadoNuevo) {
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());
        if (habitaciones.isEmpty()) throw new RuntimeException("Habitación no encontrada");

        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());
        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();
        List<Reserva> reservasGuardadas = new ArrayList<>();

        for (Habitacion h : habitaciones) {
            // Usamos la query ESTRICTA para validar cruces al crear (permite check-out y check-in el mismo día)
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getIdHabitacion(), dto.getIngreso(), dto.getEgreso()
            );

            Reserva reservaAProcesar = null;

            if (!conflictos.isEmpty()) {
                if ("OCUPADA".equals(estadoNuevo)) {
                    Optional<Reserva> miReserva = conflictos.stream()
                            .filter(r -> r.getHuesped().getId().equals(titular.getId()) && "RESERVADA".equals(r.getEstado()))
                            .findFirst();

                    if (miReserva.isPresent()) {
                        reservaAProcesar = miReserva.get();
                        reservaAProcesar.setEstado("OCUPADA");
                        reservaAProcesar.setIngreso(dto.getIngreso());
                        reservaAProcesar.setEgreso(dto.getEgreso());
                    } else {
                        throw new RuntimeException("Habitación " + h.getNumero() + " ocupada por otro huésped.");
                    }
                } else {
                    throw new RuntimeException("Habitación " + h.getNumero() + " ya reservada en esas fechas.");
                }
            }

            if (reservaAProcesar == null) {
                reservaAProcesar = new Reserva();
                reservaAProcesar.setIngreso(dto.getIngreso());
                reservaAProcesar.setEgreso(dto.getEgreso());
                reservaAProcesar.setHuesped(titular);
                reservaAProcesar.setEstado(estadoNuevo);
                reservaAProcesar.setHabitacion(h);
            }

            reservaAProcesar = reservaRepository.save(reservaAProcesar);

            if ("OCUPADA".equals(estadoNuevo)) {
                Estadia estadia = Estadia.builder()
                        .huesped(titular)
                        .habitacion(h)
                        .reserva(reservaAProcesar)
                        .fechaHoraIngreso(LocalDateTime.now())
                        .fechaIngreso(reservaAProcesar.getIngreso().toString())
                        .fechaEgreso(reservaAProcesar.getEgreso().toString())
                        .itemsConsumo(new ArrayList<>())
                        .build();
                estadiaRepository.save(estadia);
            }

            if (reservaAProcesar.getIdReserva() != null && "OCUPADA".equals(estadoNuevo) && reservaAProcesar.getOcupantes() != null) {
                reservaAProcesar.getOcupantes().clear();
            }

            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getIdHabitacion())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getIdHabitacion());
                for (OcupanteDTO oDto : listaDTOs) {
                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {
                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(oDto.getApellido() != null ? oDto.getApellido() : "SIN_APELLIDO");
                        ocupante.setDocumento(oDto.getDni() != null ? oDto.getDni() : "00000000");
                        ocupante.setTelefono("-");
                        ocupante.setTipoDocumento(TipoDocumento.DNI);
                        ocupante.setReserva(reservaAProcesar);
                        ocupante.setHabitacion(h);

                        if (reservaAProcesar.getOcupantes() == null) reservaAProcesar.setOcupantes(new ArrayList<>());
                        reservaAProcesar.getOcupantes().add(ocupante);
                    }
                }
            }
            reservasGuardadas.add(reservaRepository.save(reservaAProcesar));
        }
        return reservasGuardadas;
    }

    private Huesped obtenerOGuardarHuesped(ReservaDTO.DatosHuespedReserva datos) {
        Huesped huesped = huespedRepository.findByDocumento(datos.getDocumento());
        if (huesped == null) {
            huesped = Huesped.builder()
                    .nombre(datos.getNombre() != null ? datos.getNombre() : "SIN_NOMBRE")
                    .apellido(datos.getApellido() != null ? datos.getApellido() : "SIN_APELLIDO")
                    .documento(datos.getDocumento())
                    .telefono(datos.getTelefono())
                    .fechaNacimiento(LocalDate.of(2000, 1, 1))
                    .nacionalidad("Arg")
                    .ocupacion("-")
                    .email("temp_" + datos.getDocumento() + "@hotel.com")
                    .posicionIVA(RazonSocial.Consumidor_Final)
                    .build();
            return huespedRepository.saveAndFlush(huesped);
        } else {
            if (datos.getTelefono() != null && !datos.getTelefono().isEmpty()) {
                huesped.setTelefono(datos.getTelefono());
                return huespedRepository.save(huesped);
            }
        }
        return huesped;
    }
}