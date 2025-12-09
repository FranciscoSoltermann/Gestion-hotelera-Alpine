package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.GrillaHabitacionDTO;
import java.time.LocalDate;
import java.util.List;
/**
 * Interfaz para la gestión de habitaciones.
 */
public interface GestorHabitacion {

    /**
     * Obtiene el estado de todas las habitaciones en un rango de fechas.
     * @param fechaDesde Fecha de inicio del rango.
     * @param fechaHasta Fecha de fin del rango.
     * @return Lista de GrillaHabitacionDTO con el estado diario de cada habitación.
     */
    List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta);
}