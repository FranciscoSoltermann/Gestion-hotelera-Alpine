package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Representa una línea de detalle dentro de una factura (costo de estadía, minibar, servicio).")
public class ItemFacturableDTO {

    @Schema(description = "Descripción del ítem facturado.", example = "Alojamiento Habitación 105 / Servicio Lavandería")
    @NotBlank(message = "La descripción del item es obligatoria.")
    @Size(min = 3, max = 150, message = "La descripción debe tener entre 3 y 150 caracteres.")
    private String descripcion;

    @Schema(description = "Unidad de medida (ej: días, unidades, horas).", example = "días")
    @Size(max = 20, message = "La unidad de medida no puede exceder 20 caracteres.")
    private String medida;

    @Schema(description = "Cantidad consumida o de unidades.", example = "3")
    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidad;

    @Schema(description = "Precio del ítem sin impuestos por unidad.", example = "15000.00")
    @NotNull(message = "El precio unitario es obligatorio.")
    @DecimalMin(value = "0.00", message = "El precio unitario debe ser positivo o cero.")
    private Float precioUnitario;

    @Schema(description = "Subtotal calculado (Cantidad * Precio Unitario).", example = "45000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Float subtotal;

    @Schema(description = "Indica si este ítem es el cargo base por la estadía o habitación.", example = "true")
    private boolean esEstadia;

    @Schema(description = "ID de referencia del ítem (ej: ID del Consumo original, o ID de la Estadia).", example = "12345")
    private Long referenciaId;
}