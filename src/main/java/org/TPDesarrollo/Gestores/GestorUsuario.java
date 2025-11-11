package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.Clases.Usuario;
import org.TPDesarrollo.DTOs.UsuarioDTO;
import org.TPDesarrollo.DAOs.UsuarioDAO;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestorUsuario {

    @Autowired
    private UsuarioDAO usuarioRepo;

    @Transactional(readOnly = true)
    public boolean autenticarUsuario(String nombre, String contrasenia)
            throws UsuarioNoEncontrado, ContraseniaInvalida {

        Usuario usuarioAlmacenado = usuarioRepo.findByNombre(nombre)
                .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + nombre));

        if (!usuarioAlmacenado.getContrasenia().equals(contrasenia)) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }

        return true;
    }

    public Usuario iniciarSesion(String nombre, String contrasenia) {
        return usuarioRepo.findByNombreAndContrasenia(nombre, contrasenia).orElse(null);
    }
    public Usuario registrarUsuario(UsuarioDTO datos) {
        // 1. Verifica si el usuario ya existe
        if (usuarioRepo.findByNombre(datos.getNombre()).isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + datos.getNombre() + "' ya existe.");
        }

        // 2. Crea el nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(datos.getNombre());

        // 🚨 ALERTA DE SEGURIDAD: ¡Estás guardando contraseñas en texto plano!
        // Esto es muy inseguro. Deberías encriptarlas (ver nota abajo).
        nuevoUsuario.setContrasenia(datos.getContrasenia());
        nuevoUsuario.setRol("ADMIN"); // Asigna un rol por defecto

        // 3. Guarda y devuelve el usuario
        return usuarioRepo.save(nuevoUsuario);
    }
}
