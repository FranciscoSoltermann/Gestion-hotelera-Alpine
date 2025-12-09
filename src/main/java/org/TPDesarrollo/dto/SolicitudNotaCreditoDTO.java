package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO para solicitar la creación de una Nota de Crédito.")
public class SolicitudNotaCreditoDTO {

    @Schema(description = "Lista de IDs de las facturas que se desean anular.", example = "[10, 11]")
    @NotEmpty(message = "Debe seleccionar al menos una factura para anular.")
    private List<Long> idsFacturas; // Asumiendo que Factura usa Long como ID


}