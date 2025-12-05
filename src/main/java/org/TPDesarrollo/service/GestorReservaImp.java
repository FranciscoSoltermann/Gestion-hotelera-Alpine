package org.TPDesarrollo.service;

import org.TPDesarrollo.clases.*;
import org.TPDesarrollo.dtos.OcupanteDTO;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
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
public class GestorReservaImp implements GestorReserva {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HuespedRepository huespedRepository;

    public GestorReservaImp(HabitacionRepository hr, ReservaRepository rr, HuespedRepository phr) {
        this.habitacionRepository = hr;
        this.reservaRepository = rr;
        this.huespedRepository = phr;
    }

    @Override
    @Transactional
    public List<Reserva> crearReserva(ReservaDTO dto) {
        return procesarReserva(dto, false);
    }

    @Override
    @Transactional
    public List<Reserva> crearOcupacion(ReservaDTO dto) {
        return procesarReserva(dto, true);
    }

    private List<Reserva> procesarReserva(ReservaDTO dto, boolean esOcupacion) {

        if (dto.getIngreso().isAfter(dto.getEgreso())) {
            throw new IllegalArgumentException("La fecha de ingreso debe ser anterior al egreso");
        }

        List<Habitacion> habitaciones = habitacionRepository.findAllById(dto.getHabitaciones());
        if (habitaciones.size() != dto.getHabitaciones().size()) {
            throw new RuntimeException("Alguna habitación solicitada no existe");
        }

        Huesped titular = obtenerOGuardarHuesped(dto.getHuesped());
        Map<Integer, List<OcupanteDTO>> mapaOcupantes = dto.getOcupantesPorHabitacion();
        List<Reserva> reservasGuardadas = new ArrayList<>();

        for (Habitacion h : habitaciones) {
            List<Reserva> conflictos = reservaRepository.encontrarSolapamientos(
                    h.getId(), dto.getIngreso(), dto.getEgreso()
            );

            Reserva reservaAProcesar = null;

            if (!conflictos.isEmpty()) {
                Optional<Reserva> miReservaPrevia = conflictos.stream()
                        .filter(r -> r.getIdPersona().equals(titular.getId()))
                        .findFirst();

                if (miReservaPrevia.isPresent()) {
                    reservaAProcesar = miReservaPrevia.get();
                } else {
                    throw new RuntimeException("La habitación " + h.getNumero() + " ya está reservada por otro huésped en esas fechas.");
                }
            }

            if (reservaAProcesar == null) {
                reservaAProcesar = Reserva.builder()
                        .ingreso(dto.getIngreso())
                        .egreso(dto.getEgreso())
                        .idPersona(titular.getId())
                        .habitacion(h)
                        .build();
            }

            // --- LÓGICA DE ESTADO ---
            if (esOcupacion) {
                h.setEstado(EstadoHabitacion.OCUPADA);
                habitacionRepository.save(h);
            }

            // --- CARGA DE ACOMPAÑANTES ---
            if (mapaOcupantes != null && mapaOcupantes.containsKey(h.getId())) {
                List<OcupanteDTO> listaDTOs = mapaOcupantes.get(h.getId());
                for (OcupanteDTO oDto : listaDTOs) {
                    if (oDto.getNombre() != null && !oDto.getNombre().isBlank()) {
                        Ocupante ocupante = new Ocupante();
                        ocupante.setNombre(oDto.getNombre());
                        ocupante.setApellido(oDto.getApellido());
                        ocupante.setDni(oDto.getDni());
                        ocupante.setTelefono("-");
                        ocupante.setTipoDocumento(TipoDocumento.DNI);
                        ocupante.setHabitacion(h);

                        // Método helper de la entidad (actualizado)
                        reservaAProcesar.agregarOcupante(ocupante);
                    }
                }
            }
            reservasGuardadas.add(reservaRepository.save(reservaAProcesar));
        }

        return reservasGuardadas;
    }

    private Huesped obtenerOGuardarHuesped(ReservaDTO.DatosHuespedReserva datos) {
        // (Misma lógica auxiliar que ya tenías para buscar/guardar huesped)
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
            if (datos.getTelefono() != null && !datos.getTelefono().isEmpty()) {
                huesped.setTelefono(datos.getTelefono());
                return huespedRepository.save(huesped);
            }
        }
        return huesped;
    }
}