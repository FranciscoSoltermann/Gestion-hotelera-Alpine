package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;

import java.time.LocalDate;
/**
 * DTO que representa los datos personales y de contacto de un Huésped.
 * Contiene validaciones para asegurar la integridad de los datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa los datos personales y de contacto de un Huésped.")
public class HuespedDTO {

    @Schema(description = "ID del huésped (solo lectura en POST)", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre de pila del huésped", example = "Francisco")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Schema(description = "Apellido del huésped", example = "Soltermann")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @Schema(description = "Teléfono de contacto (sin formato)", example = "3415551234")
    @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo puede contener números")
    @Size(min = 6, max = 20, message = "El teléfono debe tener entre 6 y 20 dígitos") // Mejora: agregar límites
    private String telefono;

    @Schema(description = "Tipo de documento utilizado", example = "DNI")
    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDocumento tipoDocumento;

    @Schema(description = "Número de documento (DNI, Pasaporte, etc.)", example = "35987654")
    @NotBlank(message = "El documento no puede estar vacío")
    @Pattern(regexp = "^[0-9]*$", message = "El documento solo puede contener números")
    @Size(min = 7, max = 12, message = "El documento debe tener entre 7 y 12 dígitos") // Mejora: agregar límites
    private String documento;

    @Schema(description = "Fecha de nacimiento (Formato ISO)", example = "1990-05-20")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Schema(description = "Nacionalidad del huésped", example = "Argentino")
    @NotBlank(message = "La nacionalidad es obligatoria")
    @Size(max = 50, message = "La nacionalidad no puede exceder los 50 caracteres") // Mejora: agregar límites
    private String nacionalidad;

    @Schema(description = "Correo electrónico de contacto", example = "cliente@alpine.com")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres") // Mejora: agregar límites
    private String email;

    @Schema(description = "Detalles de la dirección del huésped (requiere validación interna)")
    @Valid
    private DireccionDTO direccion;

    @Schema(description = "CUIT del huésped (si es responsable fiscal). Solo números.", example = "20359876549")
    @Pattern(regexp = "^[0-9]*$", message = "El CUIT solo puede contener números")
    @Size(max = 11, message = "El CUIT debe tener 11 dígitos") // Mejora: forzar tamaño si es un campo de CUIT
    private String cuit;

    @Schema(description = "Ocupación o profesión", example = "Ingeniero")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "La ocupación solo puede contener letras y espacios")
    @Size(max = 50, message = "La ocupación no puede exceder los 50 caracteres") // Mejora: agregar límites
    private String ocupacion;

    @Schema(description = "Posición fiscal frente al IVA", example = "CONSUMIDOR_FINAL")
    private RazonSocial posicionIVA;
}