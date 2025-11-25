package org.TPDesarrollo.dtos;

import org.TPDesarrollo.enums.EstadoHabitacion;
import java.time.LocalDate;

public class EstadoDiaDTO {
    private LocalDate fecha;
    private EstadoHabitacion estado;

    public EstadoDiaDTO(LocalDate fecha, EstadoHabitacion estado) {
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y Setters
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public EstadoHabitacion getEstado() { return estado; }
    public void setEstado(EstadoHabitacion estado) { this.estado = estado; }
}