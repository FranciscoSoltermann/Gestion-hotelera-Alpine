package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.EstadoDiaDTO;
import org.TPDesarrollo.dto.GrillaHabitacionDTO;
import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.entity.Reserva;
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
/**
 Implementación del servicio GestorHabitacion para gestionar el estado de las habitaciones.
*/
@Service
public class GestorHabitacionImp implements GestorHabitacion {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionMapper habitacionMapper;
    /**
     * Constructor que inyecta los repositorios y el mapper necesarios.
     *
     * @param habitacionRepository Repositorio de Habitaciones.
     * @param reservaRepository    Repositorio de Reservas.
     * @param habitacionMapper     Mapper para convertir entre entidades y DTOs.
     */
    @Autowired
    public GestorHabitacionImp(HabitacionRepository habitacionRepository,
                               ReservaRepository reservaRepository,
                               HabitacionMapper habitacionMapper) {
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
        this.habitacionMapper = habitacionMapper;
    }
    /**
     * Obtiene el estado de las habitaciones en un rango de fechas.
     *
     * @param fechaDesde Fecha de inicio del rango.
     * @param fechaHasta Fecha de fin del rango.
     * @return Lista de GrillaHabitacionDTO con el estado de cada habitación por día.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        // 1. Validaciones de fechas (Igual que tu original)
        if (fechaDesde == null || fechaHasta == null)
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");

        if (fechaDesde.isAfter(fechaHasta))
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a hasta");

        // 2. Recuperar datos
        List<Habitacion> todas = habitacionRepository.findAll();
        // Asegúrate de que este método existe en tu repositorio
        List<Reserva> reservasSolapadas = reservaRepository.encontrarReservasEnRango(fechaDesde, fechaHasta);

        // 3. Agrupar reservas por ID de habitación (Optimización para no recorrer la lista mil veces)
        Map<Integer, List<Reserva>> reservasPorHab = new HashMap<>();
        for (Reserva r : reservasSolapadas) {
            // Verificamos que tenga habitación asignada para evitar NullPointer
            if (r.getHabitacion() != null) {
                reservasPorHab
                        .computeIfAbsent(r.getHabitacion().getIdHabitacion(), k -> new ArrayList<>())
                        .add(r);
            }
        }

        List<GrillaHabitacionDTO> resultado = new ArrayList<>();

        // 4. Procesar cada habitación
        for (Habitacion hab : todas) {
            // Usamos el Mapper para pasar los datos básicos de la Entidad al DTO
            GrillaHabitacionDTO fila = habitacionMapper.toGrillaDto(hab);

            List<EstadoDiaDTO> estados = new ArrayList<>();
            LocalDate dia = fechaDesde;

            // Recorremos día por día
            while (!dia.isAfter(fechaHasta)) {
                EstadoHabitacion estadoDelDia = EstadoHabitacion.DISPONIBLE;

                // A. VERIFICAR SI HAY RESERVA ESE DÍA
                List<Reserva> reservas = reservasPorHab.get(hab.getIdHabitacion());

                if (reservas != null) {
                    for (Reserva r : reservas) {
                        // Lógica original: Inicio inclusivo, Fin exclusivo
                        // Asegúrate que tu Entidad Reserva use getIngreso()/getEgreso() o getFechaInicio()/getFechaFin()
                        boolean diaDentroDeReserva = !dia.isBefore(r.getIngreso()) && dia.isBefore(r.getEgreso());

                        if (diaDentroDeReserva) {
                            // Convertimos el String de estado de la Reserva al Enum EstadoHabitacion
                            if ("OCUPADA".equals(r.getEstado())) {
                                estadoDelDia = EstadoHabitacion.OCUPADA;
                            } else {
                                estadoDelDia = EstadoHabitacion.RESERVADA;
                            }
                            break; // Si encontramos reserva, paramos de buscar en este día
                        }
                    }
                }

                // B. VERIFICAR ESTADO DE LA HABITACIÓN (Solo si no hay reserva)
                // Aquí restauramos tu lógica original de fechas de mantenimiento
                if (estadoDelDia == EstadoHabitacion.DISPONIBLE) {

                    if (hab.getEstado() != EstadoHabitacion.DISPONIBLE) {
                        // Si la habitación tiene fechas de bloqueo/mantenimiento definidas
                        if (hab.getIngreso() != null && hab.getEgreso() != null) {
                            // Verificamos si "dia" cae en ese rango
                            if (!dia.isBefore(hab.getIngreso()) && dia.isBefore(hab.getEgreso())) {
                                estadoDelDia = hab.getEstado();
                            }
                        } else {
                            // Si no tiene fechas, asumimos que el estado es permanente (ej: Clausurada indefinidamente)
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