package org.TPDesarrollo.controlador;

import jakarta.validation.Valid;
import org.TPDesarrollo.clases.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.gestores.GestorUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para el login y registro de usuarios.
 * Utiliza GestorUsuario para la lógica de negocio.
 * Maneja excepciones específicas para proporcionar respuestas adecuadas.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final GestorUsuario gestorUsuario;

    @Autowired
    public UsuarioControlador(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO datos) throws UsuarioNoEncontrado, ContraseniaInvalida {

        // Recibimos el objeto Usuario del gestor
        Usuario usuarioLogueado = gestorUsuario.autenticarUsuario(datos.getNombre(), datos.getContrasenia());

        //Devolvemos el objeto, NO un String.
        //Spring lo convertirá automáticamente a JSON
        return ResponseEntity.ok(usuarioLogueado);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO datos) {
        Usuario usuarioNuevo = gestorUsuario.registrarUsuario(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }
}