package org.TPDesarrollo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HuespedDTO {

    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo puede contener números")
    private String telefono;

    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El documento no puede estar vacío")
    @Pattern(regexp = "^[0-9]*$", message = "El documento solo puede contener números")
    private String documento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La nacionalidad es obligatoria")
    private String nacionalidad;

    @Email(message = "El email debe ser válido")
    private String email;

    @Valid
    private DireccionDTO direccion;

    @Pattern(regexp = "^[0-9]*$", message = "El CUIT solo puede contener números")
    private String cuit;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "La ocupación solo puede contener letras y espacios")
    private String ocupacion;

    private RazonSocial posicionIVA;
}