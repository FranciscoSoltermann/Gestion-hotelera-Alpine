package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.*;
import org.TPDesarrollo.dtos.OcupanteDTO;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GestorReserva {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HuespedRepository huespedRepository;

    public GestorReserva(HabitacionRepository hr, ReservaRepository rr, HuespedRepository phr) {
        this.habitacionRepository = hr;
        this.reservaRepository = rr;
        this.huespedRepository = phr;
    }

    // 1. RESERVAR
    @Transactional
    public List<Reserva> crearReserva(ReservaDTO dto) {
        return procesarReserva(dto, "RESERVADA");
    }

    // 2. OCUPAR (CHECK-IN)
    @Transactional
    public List<Reserva> crearOcupacion(ReservaDTO dto) {
        return procesarReserva(dto, "OCUPADA");
    }

    // --- LÓGICA CENTRAL ---
    private List<Reserva> procesarReserva(ReservaDTO dto, String estadoNuevo) {

        // 1. Validaciones básicas
        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());
        if (habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe");
        }

        // 2. Obtener/Guardar TITULAR (Necesitamos su ID para comparar)
        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());

        // 3. Mapa de ocupantes
        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();
        List<Reserva> reservasGuardadas = new ArrayList<>();

        // 4. PROCESAR CADA HABITACIÓN
        for (Habitacion h : habitaciones) {

            // Buscamos conflictos de fecha en la BD
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getId(), dto.getIngreso(), dto.getEgreso()
            );

            Reserva reservaAProcesar = null;

            // --- LÓGICA DE DETECCIÓN DE CHECK-IN ---
            if (!conflictos.isEmpty()) {
                // Hay solapamiento. Verificamos si es el MISMO dueño haciendo Check-in.


                if ("OCUPADA".equals(estadoNuevo)) {


                    Optional<Reserva> miReservaPrevia = conflictos.stream()
                            .filter(r -> r.getIdPersona().equals(titular.getId()))
                            .filter(r -> "RESERVADA".equals(r.getEstado()))
                            .findFirst();

                    if (miReservaPrevia.isPresent()) {
                        // ¡ENCONTRADO! Es un Check-in válido.
                        // Reutilizamos la reserva existente en lugar de crear una nueva.
                        reservaAProcesar = miReservaPrevia.get();

                        // Actualizamos el estado a OCUPADA
                        reservaAProcesar.setEstado("OCUPADA");

                        // Opcional: Actualizar fechas si el Check-in es en fecha distinta a la reserva original


                    } else {
                        // El conflicto es con OTRA persona o ya está OCUPADA.
                        throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada por otro huésped.");
                    }
                } else {
                    // Si estoy intentando RESERVAR y ya hay algo, es error directo.
                    throw new RuntimeException("La habitación " + h.getNumero() + " ya está reservada en esas fechas.");
                }
            }

            // Si no había conflicto, o no era una reserva mía -> Creamos NUEVA (Walk-in)
            if (reservaAProcesar == null) {
                reservaAProcesar = new Reserva();
                reservaAProcesar.setIngreso(dto.getIngreso());
                reservaAProcesar.setEgreso(dto.getEgreso());
                reservaAProcesar.setIdPersona(titular.getId());
                reservaAProcesar.setEstado(estadoNuevo);
                reservaAProcesar.setHabitacion(h);
            }

            // --- CARGA DE ACOMPAÑANTES ---
            // (Esta lógica sirve tanto para Nueva Reserva como para Actualizar Reserva existente)
            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getId())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getId());

                for (OcupanteDTO oDto : listaDTOs) {
                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {
                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(oDto.getApellido());
                        ocupante.setDni(oDto.getDni());

                        // Esto guarda la relación en la tabla 'ocupante'
                        reservaAProcesar.agregarOcupante(ocupante);
                    }
                }
            }

            // Guardamos (Si era existente hace UPDATE, si es nueva hace INSERT)
            reservasGuardadas.add(reservaRepository.save(reservaAProcesar));
        }

        return reservasGuardadas;
    }

    // Auxiliar Huesped
    private Huesped obtenerOGuardarHuesped(ReservaDTO.DatosHuespedReserva datos) {
        Huesped huesped = huespedRepository.findByDocumento(datos.getDocumento());
        if (huesped == null) {
            huesped = new Huesped();
            huesped.setNombre(datos.getNombre());
            huesped.setApellido(datos.getApellido());
            huesped.setDocumento(datos.getDocumento());
            huesped.setTelefono(datos.getTelefono());
            try {
                huesped.setTipoDocumento(TipoDocumento.valueOf(datos.getTipoDocumento().toUpperCase()));
            } catch (Exception e) {
                huesped.setTipoDocumento(TipoDocumento.DNI);
            }
            huesped.setFechaNacimiento(LocalDate.of(2000, 1, 1));
            huesped.setNacionalidad("Arg");
            huesped.setOcupacion("-");
            huesped.setEmail("temp_" + datos.getDocumento() + "@hotel.com");
            huesped.setPosicionIVA(RazonSocial.Consumidor_Final);
            Direccion d = new Direccion(); d.setCalle("-"); d.setNumero("-"); d.setLocalidad("-"); d.setProvincia("-"); d.setPais("Argentina"); d.setCodigoPostal("0");
            huesped.setDireccion(d);
            return huespedRepository.save(huesped);
        } else {
            // Actualizar teléfono si viene nuevo
            if (datos.getTelefono() != null && !datos.getTelefono().isEmpty()) {
                huesped.setTelefono(datos.getTelefono());
                return huespedRepository.save(huesped);
            }
        }
        return huesped;
    }
}