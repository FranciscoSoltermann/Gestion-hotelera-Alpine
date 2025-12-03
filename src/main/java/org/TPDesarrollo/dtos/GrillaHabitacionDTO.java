package org.TPDesarrollo.dtos;

import java.util.List;

/**
 * DTO para representar la grilla de una habitación en el gestor de habitaciones.
 * Contiene información básica de la habitación y su estado por día.
 * Además, incluye la capacidad de la habitación.
 */
public class GrillaHabitacionDTO {
    private Integer idHabitacion;
    private String numero;
    private String tipo;
    private Integer capacidad;
    private List<EstadoDiaDTO> estadosPorDia;

    public GrillaHabitacionDTO() {}

    // Getters y Setters
    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public List<EstadoDiaDTO> getEstadosPorDia() { return estadosPorDia; }
    public void setEstadosPorDia(List<EstadoDiaDTO> estadosPorDia) { this.estadosPorDia = estadosPorDia; }
}