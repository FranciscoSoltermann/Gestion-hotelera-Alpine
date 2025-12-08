package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación/modificación de un responsable de pago (Huésped o Empresa).
 * Usado para Alta de Persona Jurídica.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación/modificación de un responsable de pago (Huésped o Empresa). Usado para Alta de Persona Jurídica.")
public class ResponsableDePagoDTO {

    @Schema(description = "ID del responsable (solo lectura).", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    // --- CAMPOS PARA LA LISTA / OUTPUT (No requieren validación estricta aquí) ---
    @Schema(description = "Nombre completo o Razón Social (OUTPUT).", example = "PEPE SA")
    private String nombreCompleto;

    @Schema(description = "Documento o CUIT (OUTPUT).", example = "20123456789")
    private String documento;

    @Schema(description = "Tipo de entidad (OUTPUT).", example = "JURIDICA")
    private String tipo;

    // --- CAMPOS PARA EL ALTA DE EMPRESA (INPUT/VALIDACIÓN) ---
    @Schema(description = "Razón social de la empresa.", example = "Alpine S.A.")
    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 3, max = 100, message = "La razón social debe tener entre 3 y 100 caracteres")
    private String razonSocial;

    @Schema(description = "CUIT del responsable.", example = "20123456789")
    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(regexp = "^[0-9]{11}$", message = "El CUIT debe tener exactamente 11 números.")
    private String cuit;

    @Schema(description = "Teléfono de contacto (solo números).", example = "3414800000")
    @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo puede contener números")
    @Size(min = 6, max = 20, message = "El teléfono debe tener entre 6 y 20 dígitos")
    private String telefono;



    @Schema(description = "Calle de la dirección.", example = "Calle Falsa")
    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 100, message = "La calle debe tener menos de 100 caracteres")
    private String calle;

    @Schema(description = "Número de la calle.", example = "123")
    @NotBlank(message = "El número de calle es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El número debe contener solo números")
    @Size(max = 8, message = "El número de calle debe tener menos de 8 dígitos")
    private String numero;

    @Schema(description = "Departamento (Opcional).", example = "3A")
    @Size(max = 10, message = "El departamento debe tener menos de 10 caracteres")
    private String departamento;

    @Schema(description = "Piso (Opcional).", example = "3")
    @Size(max = 10, message = "El piso debe tener menos de 10 caracteres")
    private String piso;

    @Schema(description = "Código Postal.", example = "2000")
    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[0-9]{4,8}$", message = "El código postal debe contener entre 4 y 8 números.")
    private String codigoPostal;

    @Schema(description = "Localidad o Ciudad.", example = "Rosario")
    @NotBlank(message = "La localidad es obligatoria")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "La localidad solo puede contener letras y espacios")
    @Size(max = 50, message = "La localidad debe tener menos de 50 caracteres")
    private String localidad;

    @Schema(description = "Provincia/Estado.", example = "Santa Fe")
    @NotBlank(message = "La provincia es obligatoria")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "La provincia solo puede contener letras y espacios")
    @Size(max = 50, message = "La provincia debe tener menos de 50 caracteres")
    private String provincia;

    @Schema(description = "País.", example = "Argentina")
    @NotBlank(message = "El país es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El país solo puede contener letras y espacios")
    @Size(max = 50, message = "El país debe tener menos de 50 caracteres")
    private String pais;
}