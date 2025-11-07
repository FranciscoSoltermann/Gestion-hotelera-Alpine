package org.TPDesarrollo.DTOs;

public class UsuarioDTO {
    private String nombre;
    private String contrasenia;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    public String getNombre() { return nombre; }
    public String getContrasenia() { return contrasenia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
}
