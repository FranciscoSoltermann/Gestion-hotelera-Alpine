package org.TPDesarrollo.dto;

import lombok.Data;

@Data
public class CargaConsumoDTO {
    private String numeroHabitacion;
    private String descripcion;
    private Float precioUnitario;
    private Integer cantidad;
}