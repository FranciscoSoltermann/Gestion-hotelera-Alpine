package org.TPDesarrollo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Validaciones (@Valid, @NotBlank, etc.) -> Error 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Errores de campo (ej.: nombre vacío)
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Errores globales (ej: @AssertTrue del CUIT)
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. DNI Duplicado -> Error 409
    @ExceptionHandler(DniExistente.class)
    public ResponseEntity<Map<String, String>> handleDniExistente(DniExistente ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }

    // 3. CUIT Duplicado -> Error 409
    @ExceptionHandler(CuitExistente.class)
    public ResponseEntity<Map<String, String>> handleCuitExistente(CuitExistente ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }

    // 4. Usuario Duplicado -> Error 409
    @ExceptionHandler(UsuarioExistente.class) // O UsuarioExistenteException
    public ResponseEntity<Map<String, String>> handleUsuarioExistente(UsuarioExistente ex) {
        // Devolvemos la clave "nombre" para que se marque el campo en el formulario de registro
        return new ResponseEntity<>(Map.of("nombre", ex.getMessage()), HttpStatus.CONFLICT);
    }

    // 5. Usuario o Contraseña incorrectos -> Error 401
    @ExceptionHandler(ContraseniaInvalida.class) // O como se llame tu excepción de login
    public ResponseEntity<Map<String, String>> handleCredencialesInvalidas(Exception ex) {
        return new ResponseEntity<>(Map.of("global", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // 6. "El Paracaídas": Cualquier otro error no controlado -> Error 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        // Esto asegura que el front siempre reciba JSON, incluso si el servidor explota
        Map<String, String> error = Map.of("global", "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsuarioNoEncontrado.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoEncontrado(UsuarioNoEncontrado ex) {
        // Devolvemos un JSON con la clave "global" para que el Login lo muestre abajo
        // Usamos status 404 (Not Found) o 401 (Unauthorized)
        return new ResponseEntity<>(Map.of("global", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}