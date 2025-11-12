package org.TPDesarrollo.DTOs;

import jakarta.validation.constraints.NotBlank;
import org.TPDesarrollo.Validaciones.ValidPassword;

public class UsuarioDTO {

    // --- ¡AQUÍ ES DONDE VAN LAS ANOTACIONES! ---

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String nombre;

    @ValidPassword // Tu anotación personalizada para la contraseña
    private String contrasenia;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String contrasenia) {
        // El constructor ahora está limpio, sin anotaciones
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    // --- Getters y Setters (sin cambios) ---
    public String getNombre() { return nombre; }
    public String getContrasenia() { return contrasenia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}