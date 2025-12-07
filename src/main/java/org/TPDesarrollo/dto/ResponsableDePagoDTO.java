package org.TPDesarrollo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsableDePagoDTO {
    private Integer id;
    private String nombreCompleto;
    private String documento;
    private String tipo;
}