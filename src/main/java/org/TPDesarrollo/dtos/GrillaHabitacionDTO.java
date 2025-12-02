package org.TPDesarrollo.dtos;

import java.util.List;

public class GrillaHabitacionDTO {
    private Integer idHabitacion;
    private String numero;
    private String tipo;
    private Integer capacidad; // <--- AGREGAR ESTO
    private List<EstadoDiaDTO> estadosPorDia;

    public GrillaHabitacionDTO() {}

    // Getters y Setters
    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // <--- AGREGAR ESTOS DOS ---
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    // --------------------------

    public List<EstadoDiaDTO> getEstadosPorDia() { return estadosPorDia; }
    public void setEstadosPorDia(List<EstadoDiaDTO> estadosPorDia) { this.estadosPorDia = estadosPorDia; }
}