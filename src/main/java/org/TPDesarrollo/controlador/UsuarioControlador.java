package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dto.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.service.GestorUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para el login y registro de usuarios.
 * Utiliza GestorUsuario para la lógica de negocio.
 * Maneja excepciones específicas para proporcionar respuestas adecuadas.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios y Autenticación", description = "Endpoints para el inicio de sesión y el registro de nuevos usuarios del sistema.")
public class UsuarioControlador {

    private final GestorUsuario gestorUsuario;
    /**
     * Constructor del controlador de usuarios.
     */
    @Autowired
    public UsuarioControlador(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }
    /**
     * Endpoint para el inicio de sesión de un usuario.
     * @param datos Objeto UsuarioDTO con las credenciales de inicio de sesión.
     * @return Respuesta HTTP con el usuario autenticado o error en la operación.
     * @throws UsuarioNoEncontrado Si el nombre de usuario no existe.
     * @throws ContraseniaInvalida Si la contraseña es incorrecta.
     */
    @Operation(summary = "Inicio de Sesión", description = "Autentica un usuario con nombre y contraseña. Devuelve el objeto Usuario si las credenciales son válidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso. Retorna el objeto Usuario logueado."),
            @ApiResponse(responseCode = "404", description = "Error: Nombre de usuario no encontrado.",
                    content = @Content(schema = @Schema(implementation = String.class))), // Asumimos que la excepción la captura el GlobalExceptionHandler y devuelve JSON
            @ApiResponse(responseCode = "401", description = "Error: Contraseña inválida.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    /**
     * Maneja la solicitud POST para el inicio de sesión.
     * @param datos Objeto UsuarioDTO con las credenciales del usuario
     * @return ResponseEntity con el usuario autenticado o error.
     * @throws UsuarioNoEncontrado Si el nombre de usuario no existe.
     * @throws ContraseniaInvalida Si la contraseña es incorrecta.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO datos) throws UsuarioNoEncontrado, ContraseniaInvalida {

        // Recibimos el objeto Usuario del gestor
        Usuario usuarioLogueado = gestorUsuario.autenticarUsuario(datos.getNombre(), datos.getContrasenia());

        //Devolvemos el objeto, NO un String.
        //Spring lo convertirá automáticamente a JSON
        return ResponseEntity.ok(usuarioLogueado);
    }
    /**
     * Endpoint para registrar un nuevo usuario.
     * @param datos Objeto UsuarioDTO con los datos del nuevo usuario.
     * @return Respuesta HTTP con el usuario creado o error en la operación.
     */
    @Operation(summary = "Registro de nuevo Usuario", description = "Crea una nueva cuenta de usuario en el sistema. Requiere validación de contraseña y campos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente. Retorna el nuevo objeto Usuario."),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos (ej: nombre de usuario ya existe, contraseña no cumple requisitos).")
    })
    /**
     * Maneja la solicitud POST para registrar un nuevo usuario.
     * @param datos Objeto UsuarioDTO con los detalles del nuevo usuario
     * @return ResponseEntity con el usuario creado o error.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO datos) {
        Usuario usuarioNuevo = gestorUsuario.registrarUsuario(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }
}