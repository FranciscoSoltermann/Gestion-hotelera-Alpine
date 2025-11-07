package org.TPDesarrollo.Controlador;

import org.TPDesarrollo.Clases.Usuario;
import org.TPDesarrollo.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/login")
    public String login(@RequestParam String nombre, @RequestParam String contrasena) {
        Usuario usuario = usuarioServicio.iniciarSesion(nombre, contrasena);
        if (usuario != null) {
            return "Inicio de sesión exitoso: " + usuario.getNombre();
        } else {
            return "Usuario o contraseña incorrectos";
        }
    }
}
