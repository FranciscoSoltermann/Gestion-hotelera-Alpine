package org.TPDesarrollo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemFacturableDTO {
        private String descripcion;
        private String medida;
        private Integer cantidad;
        private Float precioUnitario;
        private Float subtotal;
        private boolean esEstadia;
        private Long referenciaId;
}