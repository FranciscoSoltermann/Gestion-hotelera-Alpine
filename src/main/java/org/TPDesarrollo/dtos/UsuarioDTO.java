package org.TPDesarrollo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.TPDesarrollo.validaciones.ValidPassword;

/**
 * DTO para la transferencia de datos de usuario.
 * Incluye validaciones para los campos nombre y contrasenia.
 * Utiliza una anotación personalizada @ValidPassword para validar la contraseña.
 */
public class UsuarioDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 5, max = 20, message = "El usuario debe tener entre 5 y 20 caracteres")
    private String nombre;

    @ValidPassword
    private String contrasenia;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    //Getters y Setters
    public String getNombre() { return nombre; }
    public String getContrasenia() { return contrasenia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}