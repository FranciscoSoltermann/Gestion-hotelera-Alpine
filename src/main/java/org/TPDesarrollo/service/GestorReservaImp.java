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

    // --- MÉTODOS PÚBLICOS DE LA INTERFAZ ---

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

    // --- LÓGICA CENTRAL (Tu código original restaurado) ---

    private List<Reserva> procesarReserva(ReservaDTO dto, String estadoNuevo) {

        // 1. Validaciones básicas
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        // Buscamos todas las habitaciones por sus IDs
        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());

        if (habitaciones.isEmpty() || habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe o la lista está vacía");
        }

        // 2. Obtener/Guardar TITULAR
        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());

        // 3. Preparar variables
        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();
        List<Reserva> reservasGuardadas = new ArrayList<>();

        // 4. PROCESAR CADA HABITACIÓN
        for (Habitacion h : habitaciones) {

            // Buscamos conflictos de fecha en la BD
            // NOTA: Asegúrate de tener este método en tu ReservaRepository
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getIdHabitacion(), dto.getIngreso(), dto.getEgreso()
            );

            Reserva reservaAProcesar = null;

            // --- LÓGICA DE DETECCIÓN DE CHECK-IN ---
            if (!conflictos.isEmpty()) {

                if ("OCUPADA".equals(estadoNuevo)) {
                    // Hay solapamiento. Verificamos si es el MISMO dueño haciendo Check-in.
                    Optional<Reserva> miReservaPrevia = conflictos.stream()
                            // Comparamos IDs (usando getters de objeto)
                            .filter(r -> r.getHuesped().getId().equals(titular.getId()))
                            .filter(r -> "RESERVADA".equals(r.getEstado()))
                            .findFirst();

                    if (miReservaPrevia.isPresent()) {
                        // ¡ENCONTRADO! Reutilizamos la reserva existente.
                        reservaAProcesar = miReservaPrevia.get();
                        reservaAProcesar.setEstado("OCUPADA");

                        // Opcional: Actualizar fechas si cambiaron ligeramente
                        // reservaAProcesar.setIngreso(dto.getIngreso());
                        // reservaAProcesar.setEgreso(dto.getEgreso());

                    } else {
                        // El conflicto es con OTRA persona o ya está OCUPADA.
                        throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada por otro huésped.");
                    }
                } else {
                    // Si estoy intentando RESERVAR y ya hay algo, es error directo.
                    throw new RuntimeException("La habitación " + h.getNumero() + " ya está reservada en esas fechas.");
                }
            }

            // Si no había conflicto, o no era una reserva previa -> Creamos NUEVA (Walk-in o Reserva nueva)
            if (reservaAProcesar == null) {
                reservaAProcesar = new Reserva();
                // Ajusta los nombres de los setters según tu Entidad Reserva (ej: setFechaInicio vs setIngreso)
                reservaAProcesar.setIngreso(dto.getIngreso());
                reservaAProcesar.setEgreso(dto.getEgreso());
                reservaAProcesar.setHuesped(titular);
                reservaAProcesar.setEstado(estadoNuevo);
                reservaAProcesar.setHabitacion(h);
            }

            // --- CARGA DE ACOMPAÑANTES (Ocupantes) ---
            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getIdHabitacion())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getIdHabitacion());

                for (OcupanteDTO oDto : listaDTOs) {
                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {

                        // Asegúrate de tener la entidad Ocupante importada
                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(oDto.getApellido());
                        ocupante.setDocumento(oDto.getDni());
                        ocupante.setTelefono("-");
                        ocupante.setTipoDocumento(TipoDocumento.DNI);

                        // Relaciones
                        ocupante.setReserva(reservaAProcesar);
                        ocupante.setHabitacion(h); // Tu lógica original pedía esto

                        // Agregamos a la lista de la reserva (asegúrate que Reserva tenga la lista inicializada)
                        if (reservaAProcesar.getOcupantes() == null) {
                            reservaAProcesar.setOcupantes(new ArrayList<>());
                        }
                        reservaAProcesar.getOcupantes().add(ocupante);
                    }
                }
            }

            // Guardamos (update o insert)
            reservasGuardadas.add(reservaRepository.save(reservaAProcesar));
        }

        return reservasGuardadas;
    }

    // --- MÉTODOS AUXILIARES ---

    private Huesped obtenerOGuardarHuesped(ReservaDTO.DatosHuespedReserva datos) {
        Huesped huesped = huespedRepository.findByDocumento(datos.getDocumento());

        if (huesped == null) {
            huesped = Huesped.builder()
                    .nombre(datos.getNombre())
                    .apellido(datos.getApellido())
                    .documento(datos.getDocumento())
                    .telefono(datos.getTelefono())
                    // Valores por defecto de tu código original
                    .fechaNacimiento(LocalDate.of(2000, 1, 1))
                    .nacionalidad("Arg")
                    .ocupacion("-")
                    .email("temp_" + datos.getDocumento() + "@hotel.com")
                    .posicionIVA(RazonSocial.Consumidor_Final)
                    .build();

            // Manejo de Enum TipoDocumento con seguridad
            try {
                huesped.setTipoDocumento(TipoDocumento.valueOf(datos.getTipoDocumento().toUpperCase()));
            } catch (Exception e) {
                huesped.setTipoDocumento(TipoDocumento.DNI);
            }

            // Manejo de Dirección (Entidad o Embeddable)
            // Asumiendo que Direccion es una clase @Embeddable o Entity
            Direccion d = new Direccion();
            d.setCalle("-"); d.setNumero("-"); d.setLocalidad("-");
            d.setProvincia("-"); d.setPais("Argentina"); d.setCodigoPostal("0");
            huesped.setDireccion(d);

            return huespedRepository.save(huesped);
        } else {
            // Actualizar teléfono si viene nuevo (Tu lógica original)
            if (datos.getTelefono() != null && !datos.getTelefono().isEmpty()) {
                huesped.setTelefono(datos.getTelefono());
                return huespedRepository.save(huesped);
            }
        }
        return huesped;
    }
}