package org.TPDesarrollo.service;

import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import java.time.LocalDate;
import java.util.List;

public interface GestorHabitacion {

    List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta);
}