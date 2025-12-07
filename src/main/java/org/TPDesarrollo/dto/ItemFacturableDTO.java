package org.TPDesarrollo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemFacturableDTO {
    private String descripcion;
    private Integer cantidad;
    private Float precioUnitario;
    private Float subtotal;
    private boolean esEstadia; // Para distinguir si es el cargo de la habitación o un consumo
    private Long referenciaId; // ID del consumo si aplica
}