package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.dtos.EstadoDiaDTO;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestorHabitacion {

    private final HabitacionRepository habitacionRepository;

    @Autowired
    public GestorHabitacion(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null) throw new IllegalArgumentException("Fechas obligatorias");
        if (fechaDesde.isAfter(fechaHasta)) throw new IllegalArgumentException("Fecha desde posterior a hasta");

        List<Habitacion> todasLasHabitaciones = habitacionRepository.findAll();
        List<GrillaHabitacionDTO> grilla = new ArrayList<>();

        for (Habitacion hab : todasLasHabitaciones) {
            GrillaHabitacionDTO fila = new GrillaHabitacionDTO();
            fila.setIdHabitacion(hab.getId());
            fila.setNumero(hab.getNumero()); // Ahora es String, no necesita conversión
            fila.setTipo(hab.getClass().getSimpleName());

            List<EstadoDiaDTO> estadosDias = new ArrayList<>();
            LocalDate diaActual = fechaDesde;

            while (!diaActual.isAfter(fechaHasta)) {
                EstadoHabitacion estadoDelDia = EstadoHabitacion.DISPONIBLE;

                // LÓGICA CAMBIADA PARA TU BD:
                // Miramos directamente los campos 'ingreso' y 'egreso' de la habitación
                if (hab.getIngreso() != null && hab.getEgreso() != null) {
                    if (!diaActual.isBefore(hab.getIngreso()) && !diaActual.isAfter(hab.getEgreso())) {
                        // Si el día cae dentro del rango de ocupación actual de la habitación
                        estadoDelDia = hab.getEstado(); // Usamos el estado actual (ej: OCUPADA)
                    }
                }

                estadosDias.add(new EstadoDiaDTO(diaActual, estadoDelDia));
                diaActual = diaActual.plusDays(1);
            }

            fila.setEstadosPorDia(estadosDias);
            grilla.add(fila);
        }
        return grilla;
    }
}