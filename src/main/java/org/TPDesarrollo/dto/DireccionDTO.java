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
public class DireccionDTO {

    @Schema(description = "País de la dirección", example = "Argentina")
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país debe tener menos de 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El país solo puede contener letras y espacios")
    private String pais;

    @Schema(description = "Provincia/Estado de la dirección", example = "Santa Fe")
    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 50, message = "La provincia debe tener menos de 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "La provincia solo puede contener letras y espacios")
    private String provincia;

    @Schema(description = "Localidad o Ciudad", example = "Rosario")
    @NotBlank(message = "La localidad es obligatoria")
    @Size(max = 50, message = "La localidad debe tener menos de 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "La localidad solo puede contener letras y espacios")
    private String localidad;

    @Schema(description = "Nombre de la calle", example = "Avenida siempre viva")
    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 100, message = "La calle debe tener menos de 100 caracteres")
    private String calle;

    @Schema(description = "Número de puerta o casa", example = "742")
    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El número debe contener solo números")
    @Size(max = 8, message = "El número de calle debe tener menos de 8 dígitos")
    private String numero;

    @Schema(description = "Departamento (Opcional)", example = "3A")
    @Size(max = 10, message = "El departamento debe tener menos de 10 caracteres")
    private String departamento;

    @Schema(description = "Piso (Opcional)", example = "3")
    @Size(max = 10, message = "El piso debe tener menos de 10 caracteres")
    private String piso;

    @Schema(description = "Código Postal", example = "2000")
    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[0-9]{4,8}$", message = "El código postal debe contener entre 4 y 8 números.")
    private String codigoPostal;
}