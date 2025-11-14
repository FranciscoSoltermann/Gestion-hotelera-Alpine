package org.TPDesarrollo.Controlador;

import jakarta.validation.Valid;
import org.TPDesarrollo.Clases.Usuario;
import org.TPDesarrollo.DTOs.UsuarioDTO; // 1. Importa y usa tu DTO
import org.TPDesarrollo.Gestores.GestorUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;       // 2. Importa HttpStatus
import org.springframework.http.ResponseEntity;  // 2. Importa ResponseEntity
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioControlador {

    @Autowired
    private GestorUsuario usuarioServicio;

    @PostMapping("/login")
    // Acepta el DTO en lugar de la Entidad completa
    public ResponseEntity<?> login(@RequestBody UsuarioDTO datos) {
        Usuario usuario = usuarioServicio.iniciarSesion(datos.getNombre(), datos.getContrasenia());

        //  Si el servicio devuelve null (login fallido)
        if (usuario == null) {
            // Devuelve un error 401 (No Autorizado) con un mensaje claro
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }

        //  Si el login es exitoso, devuelve 200 OK con el objeto Usuario
        return ResponseEntity.ok(usuario);
    }
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO datos) {

        // Ahora, si la validación (@ValidPassword) falla,
        // o si registrarUsuario lanza "UsuarioExistenteException",
        // GlobalExceptionHandler los atrapará automáticamente.


        Usuario usuarioNuevo = usuarioServicio.registrarUsuario(datos);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }
}