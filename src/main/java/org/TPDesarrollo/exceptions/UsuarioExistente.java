package org.TPDesarrollo.exceptions;

// No necesitas @ResponseStatus si usas el GlobalExceptionHandler
public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente(String message) {
        super(message);
    }
}