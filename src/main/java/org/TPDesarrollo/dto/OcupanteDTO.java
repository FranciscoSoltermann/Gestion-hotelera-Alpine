package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa a un ocupante específico dentro de una habitación.")
public class OcupanteDTO {

    @Schema(description = "Nombre del ocupante.", example = "Juan")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Schema(description = "Apellido del ocupante.", example = "Perez")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @Schema(description = "Número de Documento de Identidad (DNI).", example = "30123456")
    @NotBlank(message = "El DNI del ocupante es obligatorio")
    // Validación mejorada para forzar 7 u 8 dígitos numéricos (estándar argentino)
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe contener 7 u 8 números")
    private String dni;
}