package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.entity.Reserva;
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
    public GestorHabitacionImp(HabitacionRepository habitacionRepository,
                               ReservaRepository reservaRepository,
                               HabitacionMapper habitacionMapper) {
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
            if (r.getHabitacion() != null) {
                reservasPorHab
                        .computeIfAbsent(r.getHabitacion().getId(), k -> new ArrayList<>())
                        .add(r);
            }
        }

        List<GrillaHabitacionDTO> resultado = new ArrayList<>();

        for (Habitacion hab : todas) {
            GrillaHabitacionDTO fila = habitacionMapper.toGrillaDto(hab);
            List<EstadoDiaDTO> estados = new ArrayList<>();
            LocalDate dia = fechaDesde;

            while (!dia.isAfter(fechaHasta)) {
                EstadoHabitacion estadoDelDia = EstadoHabitacion.DISPONIBLE;

                List<Reserva> reservas = reservasPorHab.get(hab.getId());
                boolean tieneReserva = false;
                if (reservas != null) {
                    for (Reserva r : reservas) {
                        if (!dia.isBefore(r.getIngreso()) && dia.isBefore(r.getEgreso())) {
                            tieneReserva = true;
                            break;
                        }
                    }
                }

                if (tieneReserva) {
                    estadoDelDia = EstadoHabitacion.RESERVADA;
                }

                if (hab.getEstado() != EstadoHabitacion.DISPONIBLE) {
                    if (dia.equals(LocalDate.now())) {
                        estadoDelDia = hab.getEstado();
                    } else if (tieneReserva && hab.getEstado() == EstadoHabitacion.OCUPADA) {
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