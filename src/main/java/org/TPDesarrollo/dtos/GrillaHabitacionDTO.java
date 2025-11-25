package org.TPDesarrollo.dtos;

import java.util.List;

public class GrillaHabitacionDTO {
    private Integer idHabitacion;
    private String numero;
    private String tipo; // Para agrupar por tipo (Doble, Suite, etc.) como pide el CU
    private List<EstadoDiaDTO> estadosPorDia;

    public GrillaHabitacionDTO() {}

    // Getters y Setters
    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public List<EstadoDiaDTO> getEstadosPorDia() { return estadosPorDia; }
    public void setEstadosPorDia(List<EstadoDiaDTO> estadosPorDia) { this.estadosPorDia = estadosPorDia; }
}