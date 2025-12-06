package org.TPDesarrollo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {

    @NotBlank(message = "El país es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El país solo puede contener letras y espacios")
    private String pais;

    @NotBlank(message = "La provincia es obligatoria")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "La provincia solo puede contener letras y espacios")
    private String provincia;

    @NotBlank(message = "La localidad es obligatoria")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "La localidad solo puede contener letras y espacios")
    private String localidad;

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El número debe contener solo números")
    private String numero;

    private String departamento;
    private String piso;

    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
}