package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.EstadoDiaDTO;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
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
public class GestorHabitacion {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public GestorHabitacion(HabitacionRepository habitacionRepository, ReservaRepository reservaRepository) {
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null)
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");

        if (fechaDesde.isAfter(fechaHasta))
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a hasta");

        // 1) Traer todas las habitaciones
        List<Habitacion> todas = habitacionRepository.findAll();

        // 2) Traer reservas en el rango (de clientes)
        List<Reserva> reservasSolapadas = reservaRepository.encontrarReservasEnRango(fechaDesde, fechaHasta);

        // Agrupar reservas por ID de habitación para acceso rápido
        Map<Integer, List<Reserva>> reservasPorHab = new HashMap<>();

        for (Reserva r : reservasSolapadas) {
            // Asegúrate que tu getter en Reserva sea getHabitaciones()
            List<Habitacion> habitacionesDeEstaReserva = r.getHabitaciones();

            if (habitacionesDeEstaReserva != null) {
                for (Habitacion h : habitacionesDeEstaReserva) {
                    reservasPorHab
                            .computeIfAbsent(h.getId(), k -> new ArrayList<>())
                            .add(r);
                }
            }
        }

        List<GrillaHabitacionDTO> resultado = new ArrayList<>();

        for (Habitacion hab : todas) {

            GrillaHabitacionDTO fila = new GrillaHabitacionDTO();
            fila.setIdHabitacion(hab.getId());
            fila.setNumero(hab.getNumero());
            fila.setTipo(hab.getClass().getSimpleName());

            List<EstadoDiaDTO> estados = new ArrayList<>();
            LocalDate dia = fechaDesde;

            // Recorremos día a día en el rango solicitado
            while (!dia.isAfter(fechaHasta)) {

                EstadoHabitacion estadoDelDia = EstadoHabitacion.DISPONIBLE;

                // -----------------------------------------------------------
                // 1. VERIFICAR RESERVAS (Prioridad 1: Clientes)
                // -----------------------------------------------------------
                List<Reserva> reservas = reservasPorHab.get(hab.getId());

                if (reservas != null) {
                    for (Reserva r : reservas) {
                        // Verificamos si el día cae dentro del rango de la reserva
                        boolean dentro = !dia.isBefore(r.getFechaIngreso()) && !dia.isAfter(r.getFechaEgreso());

                        if (dentro) {
                            estadoDelDia = r.getEstadoHabitacion();
                            break;
                        }
                    }
                }

                // -----------------------------------------------------------
                // 2. VERIFICAR ESTADO PROPIO DE LA HABITACIÓN (Mantenimiento)
                // -----------------------------------------------------------
                // Si no hay reserva (sigue DISPONIBLE), chequeamos si la habitación
                // tiene un bloqueo administrativo (ej. MANTENIMIENTO)
                if (estadoDelDia == EstadoHabitacion.DISPONIBLE) {

                    // Si el estado general en BD NO es disponible (ej: MANTENIMIENTO)
                    if (hab.getEstado() != EstadoHabitacion.DISPONIBLE) {

                        // Validamos las fechas de la habitación (ingreso/egreso en la tabla Habitacion)
                        if (hab.getIngreso() != null && hab.getEgreso() != null) {
                            // Si el día actual está dentro del rango de mantenimiento
                            if (!dia.isBefore(hab.getIngreso()) && !dia.isAfter(hab.getEgreso())) {
                                estadoDelDia = hab.getEstado(); // Asignamos MANTENIMIENTO
                            }
                        } else {
                            // Si está en MANTENIMIENTO pero no tiene fechas, asumimos bloqueo total
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