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

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final GestorUsuario gestorUsuario;

    @Autowired
    public UsuarioControlador(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }

    @PostMapping("/login")
    // Quitamos el 'throws' porque las excepciones son RuntimeException (no chequeadas)
    public ResponseEntity<?> login(@RequestBody UsuarioDTO datos) throws UsuarioNoEncontrado, ContraseniaInvalida {

        // 1. CORRECCIÓN: Recibimos el objeto Usuario del gestor
        // (Asegúrate de haber actualizado GestorUsuario para que devuelva 'Usuario' y no 'void')
        Usuario usuarioLogueado = gestorUsuario.autenticarUsuario(datos.getNombre(), datos.getContrasenia());

        // 2. CORRECCIÓN: Devolvemos el objeto, NO un String.
        // Spring lo convertirá automáticamente a JSON: {"id":1, "nombre":"fran", ...}
        return ResponseEntity.ok(usuarioLogueado);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO datos) {
        Usuario usuarioNuevo = gestorUsuario.registrarUsuario(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }
}