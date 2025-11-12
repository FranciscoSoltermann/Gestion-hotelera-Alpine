package org.TPDesarrollo.Excepciones;

// No necesitas @ResponseStatus si usas el GlobalExceptionHandler
public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente(String message) {
        super(message);
    }
}