package org.TPDesarrollo.service;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.EstadoDiaDTO;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.mappers.HabitacionMapper;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GestorHabitacionImp implements GestorHabitacion {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionMapper habitacionMapper;

    @Autowired
    public GestorHabitacionImp(HabitacionRepository habitacionRepository, ReservaRepository reservaRepository, HabitacionMapper habitacionMapper) {
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
        this.habitacionMapper = habitacionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null)
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");

        if (fechaDesde.isAfter(fechaHasta))
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a hasta");

        List<Habitacion> todas = habitacionRepository.findAll();
        List<Reserva> reservasSolapadas = reservaRepository.encontrarReservasEnRango(fechaDesde, fechaHasta);

        Map<Integer, List<Reserva>> reservasPorHab = new HashMap<>();

        for (Reserva r : reservasSolapadas) {
            Habitacion h = r.getHabitacion();
            if (h != null) {
                reservasPorHab
                        .computeIfAbsent(h.getId(), k -> new ArrayList<>())
                        .add(r);
            }
        }

        List<GrillaHabitacionDTO> resultado = new ArrayList<>();

        for (Habitacion hab : todas) {

            // Usamos el Mapper para iniciar la estructura
            GrillaHabitacionDTO fila = habitacionMapper.toGrillaDto(hab);

            List<EstadoDiaDTO> estados = new ArrayList<>();
            LocalDate dia = fechaDesde;

            while (!dia.isAfter(fechaHasta)) {

                EstadoHabitacion estadoDelDia = EstadoHabitacion.DISPONIBLE;
                List<Reserva> reservas = reservasPorHab.get(hab.getId());

                if (reservas != null) {
                    for (Reserva r : reservas) {
                        boolean dentro = !dia.isBefore(r.getFechaIngreso()) && dia.isBefore(r.getFechaEgreso());
                        if (dentro) {
                            estadoDelDia = r.getEstadoHabitacion();
                            break;
                        }
                    }
                }

                if (estadoDelDia == EstadoHabitacion.DISPONIBLE) {
                    if (hab.getEstado() != EstadoHabitacion.DISPONIBLE) {
                        if (hab.getIngreso() != null && hab.getEgreso() != null) {
                            if (!dia.isBefore(hab.getIngreso()) && dia.isBefore(hab.getEgreso())) {
                                estadoDelDia = hab.getEstado();
                            }
                        } else {
                            estadoDelDia = hab.getEstado();
                        }
                    }
                }

                estados.add(new EstadoDiaDTO(dia, estadoDelDia));
                dia = dia.plusDays(1);
            }

            fila.setEstadosPorDia(estados);
            resultado.add(fila);
        }

        return resultado;
    }
}