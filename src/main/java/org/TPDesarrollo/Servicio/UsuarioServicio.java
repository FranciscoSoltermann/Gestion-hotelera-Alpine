package org.TPDesarrollo.Servicio;

import org.TPDesarrollo.Clases.Usuario;
import org.TPDesarrollo.DTOs.UsuarioDTO;
import org.TPDesarrollo.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario iniciarSesion(String nombre, String contrasenia) {
        return usuarioRepositorio.findByNombreAndContrasenia(nombre, contrasenia).orElse(null);
    }
    public Usuario registrarUsuario(UsuarioDTO datos) {
        // 1. Verifica si el usuario ya existe
        if (usuarioRepositorio.findByNombre(datos.getNombre()).isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + datos.getNombre() + "' ya existe.");
        }

        // 2. Crea el nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(datos.getNombre());

        // 🚨 ALERTA DE SEGURIDAD: ¡Estás guardando contraseñas en texto plano!
        // Esto es muy inseguro. Deberías encriptarlas (ver nota abajo).
        nuevoUsuario.setContrasenia(datos.getContrasenia());
        nuevoUsuario.setRol("USER"); // Asigna un rol por defecto

        // 3. Guarda y devuelve el usuario
        return usuarioRepositorio.save(nuevoUsuario);
    }
}
