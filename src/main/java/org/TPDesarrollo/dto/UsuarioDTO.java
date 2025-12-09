package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.validation.ValidPassword;

/**
 * DTO utilizado para el inicio de sesión y el registro de usuarios en el sistema.
 * Contiene las credenciales necesarias para autenticar a un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO utilizado para el inicio de sesión y el registro de usuarios en el sistema.")
public class UsuarioDTO {

    @Schema(description = "Nombre de usuario único.", example = "jorge.administracion")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 5, max = 20, message = "El usuario debe tener entre 5 y 20 caracteres")
    private String nombre;

    @Schema(description = "Contraseña segura. REGLAS: Debe contener al menos 5 letras y al menos 3 números (los números no pueden ser consecutivos ni iguales).", example = "MiPass1234")
    @ValidPassword
    private String contrasenia;
}