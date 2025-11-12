package org.TPDesarrollo.Excepciones; // O donde prefieras

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Este método se activa CADA VEZ que una validación de @Valid falla
     * en CUALQUIER controlador.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Responde con un 400
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Creamos un mapa para guardar los errores: "campo" -> "mensaje"
        Map<String, String> errors = new HashMap<>();

        // 1. Recorremos los errores de campos específicos (ej.: @Pattern en "cuit")
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // 2. Recorremos los errores globales (ej: tu @AssertTrue de "isCuitConsistente")
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            // Usamos el nombre del objeto (ej.: "huespedDTO") como clave
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        return errors;
    }

    // --- Puedes agregar más manejadores aquí ---
    @ExceptionHandler(DniExistente.class)
    public ResponseEntity<Object> handleDocumentoExistenteException(DniExistente ex) {
        // Devuelve el JSON que tu modal "ActionModal" espera.
        Map<String, String> error = Map.of("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409 Conflict
    }
    // Por ejemplo, para tus excepciones personalizadas como CuitExistente
    @ExceptionHandler(UsuarioExistente.class)
    public ResponseEntity<Object> handleUsuarioExistenteException(UsuarioExistente ex) {
        Map<String, String> error = Map.of("nombre", ex.getMessage());
        // HttpStatus.CONFLICT (409) es el código semántico para "recurso ya existe"
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CuitExistente.class) // Asumiendo que tienes esta clase
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict es bueno para "ya existe"
    public Map<String, String> handleCuitExistente(CuitExistente ex) {
        return Map.of("error", ex.getMessage());
    }


}