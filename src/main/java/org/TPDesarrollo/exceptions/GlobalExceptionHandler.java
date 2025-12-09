package org.TPDesarrollo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger para imprimir errores en la consola del servidor
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Manejo de Validaciones (@Valid) -> Error 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. Errores de Lectura de JSON (JSON mal formado o tipos incorrectos) -> Error 400
    // IMPORTANTE: Esto pasa si el front manda "edad": "veinte" en vez de numero, o falta una coma.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonErrors(HttpMessageNotReadableException ex) {
        logger.error("Error leyendo JSON: ", ex); // Logueamos el error
        return new ResponseEntity<>(Map.of("global", "El formato del JSON enviado es incorrecto o contiene datos inválidos."), HttpStatus.BAD_REQUEST);
    }

    // 3. Violación de Integridad de Datos (Foreign Keys) -> Error 409
    // IMPORTANTE: Pasa si intentas borrar un usuario que tiene reservas asociadas.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDBIntegrity(DataIntegrityViolationException ex) {
        logger.error("Conflicto de base de datos: ", ex);
        return new ResponseEntity<>(Map.of("global", "No se puede realizar la operación porque el registro está siendo usado por otros datos (Integridad Referencial)."), HttpStatus.CONFLICT);
    }

    // 4. Tus Excepciones Personalizadas
    @ExceptionHandler(DniExistente.class)
    public ResponseEntity<Map<String, String>> handleDniExistente(DniExistente ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CuitExistente.class)
    public ResponseEntity<Map<String, String>> handleCuitExistente(CuitExistente ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioExistente.class)
    public ResponseEntity<Map<String, String>> handleUsuarioExistente(UsuarioExistente ex) {
        return new ResponseEntity<>(Map.of("nombre", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioNoEncontrado.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoEncontrado(UsuarioNoEncontrado ex) {
        return new ResponseEntity<>(Map.of("global", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContraseniaInvalida.class)
    public ResponseEntity<Map<String, String>> handleCredencialesInvalidas(Exception ex) {
        return new ResponseEntity<>(Map.of("global", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // 5. "El Paracaídas": Cualquier otro error no controlado -> Error 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        // ES CRUCIAL loguear esto, si no, no sabrás por qué falló el servidor
        logger.error("Error interno no controlado: ", ex);

        // En producción no se suele enviar ex.getMessage() por seguridad,
        // pero para tu TP está perfecto para debuggear.
        Map<String, String> error = Map.of("global", "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}