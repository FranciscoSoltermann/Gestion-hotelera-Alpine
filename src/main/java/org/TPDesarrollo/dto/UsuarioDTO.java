package org.TPDesarrollo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.validaciones.ValidPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 5, max = 20, message = "El usuario debe tener entre 5 y 20 caracteres")
    private String nombre;

    @ValidPassword
    private String contrasenia;
}