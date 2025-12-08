package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CargaConsumoDTO {

    @Schema(description = "Número de la habitación donde se registra el consumo.", example = "104")
    @NotBlank(message = "El número de habitación es obligatorio.")
    @Size(max = 10, message = "El número de habitación no puede exceder los 10 caracteres.")
    private String numeroHabitacion;

    @Schema(description = "Descripción del producto o servicio consumido.", example = "Coca Cola, Lavandería, Rotura Vaso.")
    @NotBlank(message = "La descripción del consumo es obligatoria.")
    @Size(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres.")
    private String descripcion;

    @Schema(description = "Precio unitario del ítem.", example = "2500.00")
    @NotNull(message = "El precio unitario es obligatorio.")
    @DecimalMin(value = "0.00", message = "El precio unitario no puede ser negativo.")
    private Float precioUnitario;

    @Schema(description = "Cantidad consumida del producto.", example = "1")
    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad mínima debe ser 1.")
    private Integer cantidad;
}