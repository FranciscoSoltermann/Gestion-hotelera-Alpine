package org.TPDesarrollo.Servicio;

import org.TPDesarrollo.Clases.Usuario;
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
}
