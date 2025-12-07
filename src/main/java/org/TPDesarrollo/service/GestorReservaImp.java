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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.TPDesarrollo.repository.EstadiaRepository;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Inyección automática de repositorios
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

    private List<Reserva> procesarReserva(ReservaDTO dto, String estadoNuevo) {

        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());

        if (habitaciones.isEmpty() || habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe o la lista está vacía");
        }

        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());

        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();
        List<Reserva> reservasGuardadas = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getIdHabitacion(), dto.getIngreso(), dto.getEgreso()
            );

            Reserva reservaAProcesar = null;

            if (!conflictos.isEmpty()) {

                if ("OCUPADA".equals(estadoNuevo)) {
                    Optional<Reserva> miReservaPrevia = conflictos.stream()
                            .filter(r -> r.getHuesped().getId().equals(titular.getId()))
                            .filter(r -> "RESERVADA".equals(r.getEstado()))
                            .findFirst();

                    if (miReservaPrevia.isPresent()) {
                        reservaAProcesar = miReservaPrevia.get();
                        reservaAProcesar.setEstado("OCUPADA");

                        reservaAProcesar.setIngreso(dto.getIngreso());
                        reservaAProcesar.setEgreso(dto.getEgreso());

                    } else {
                        throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada por otro huésped.");
                    }
                } else {
                    throw new RuntimeException("La habitación " + h.getNumero() + " ya está reservada en esas fechas.");
                }
            }

            if (reservaAProcesar == null) {
                reservaAProcesar = new Reserva();
                // Ajusta los nombres de los setters según tu Entidad Reserva (ej: setFechaInicio vs setIngreso)
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

            // --- PASO CLAVE 3: CARGA DE ACOMPAÑANTES (Ocupantes) ---
            // Si el proceso es un Check-in sobre una reserva existente, es buena práctica
            // limpiar la lista de ocupantes para evitar duplicados si se vuelven a cargar
            if (reservaAProcesar.getIdReserva() != null && "OCUPADA".equals(estadoNuevo)) {
                if (reservaAProcesar.getOcupantes() != null) {
                    reservaAProcesar.getOcupantes().clear();
                }
            }

            // --- CARGA DE ACOMPAÑANTES (Ocupantes) ---
            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getIdHabitacion())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getIdHabitacion());

                for (OcupanteDTO oDto : listaDTOs) {

                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {

                        String apellido = (oDto.getApellido() != null && !oDto.getApellido().isBlank())
                                ? oDto.getApellido() : "SIN_APELLIDO";
                        String documento = (oDto.getDni() != null && !oDto.getDni().isBlank())
                                ? oDto.getDni() : "00000000";

                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(apellido);
                        ocupante.setDocumento(documento);
                        ocupante.setTelefono("-");
                        ocupante.setTipoDocumento(TipoDocumento.DNI);
                        ocupante.setReserva(reservaAProcesar);
                        ocupante.setHabitacion(h);

                        if (reservaAProcesar.getOcupantes() == null) {
                            reservaAProcesar.setOcupantes(new ArrayList<>());
                        }
                        reservaAProcesar.getOcupantes().add(ocupante);
                    }
                }
            }

            reservasGuardadas.add(reservaRepository.save(reservaAProcesar));
        }

        return reservasGuardadas;
    }

    // --- MÉTODOS AUXILIARES ---

    private Huesped obtenerOGuardarHuesped(ReservaDTO.DatosHuespedReserva datos) {
        Huesped huesped = huespedRepository.findByDocumento(datos.getDocumento());

        if (huesped == null) {

            String apellido = (datos.getApellido() != null && !datos.getApellido().isBlank())
                    ? datos.getApellido() : "SIN_APELLIDO";
            String nombre = (datos.getNombre() != null && !datos.getNombre().isBlank())
                    ? datos.getNombre() : "SIN_NOMBRE";

            huesped = Huesped.builder()
                    .nombre(nombre)
                    .apellido(apellido)
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