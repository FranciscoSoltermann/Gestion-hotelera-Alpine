package org.TPDesarrollo.service;

import org.TPDesarrollo.clases.*;
import org.TPDesarrollo.dtos.OcupanteDTO;
import org.TPDesarrollo.dtos.ReservaDTO;
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

    // Inyectamos repositorios
    public GestorReservaImp(HabitacionRepository hr, ReservaRepository rr, HuespedRepository phr) {
        this.habitacionRepository = hr;
        this.reservaRepository = rr;
        this.huespedRepository = phr;
    }

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
                if ("OCUPADA".equals(estadoNuevo)) {
                    Optional<Reserva> miReservaPrevia = conflictos.stream()
                            .filter(r -> r.getIdPersona().equals(titular.getId()))
                            .filter(r -> "RESERVADA".equals(r.getEstado()))
                            .findFirst();

                    if (miReservaPrevia.isPresent()) {
                        reservaAProcesar = miReservaPrevia.get();
                        reservaAProcesar.setEstado("OCUPADA");
                    } else {
                        throw new RuntimeException("La habitación " + h.getNumero() + " ya está ocupada por otro huésped.");
                    }
                } else {
                    throw new RuntimeException("La habitación " + h.getNumero() + " ya está reservada en esas fechas.");
                }
            }

            if (reservaAProcesar == null) {
                reservaAProcesar = new Reserva();
                reservaAProcesar.setIngreso(dto.getIngreso());
                reservaAProcesar.setEgreso(dto.getEgreso());
                reservaAProcesar.setIdPersona(titular.getId());
                reservaAProcesar.setEstado(estadoNuevo);
                reservaAProcesar.setHabitacion(h);
            }

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
                        ocupante.setReserva(reservaAProcesar);
                        ocupante.setHabitacion(h);

                        reservaAProcesar.agregarOcupante(ocupante);
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
            // Valores por defecto
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