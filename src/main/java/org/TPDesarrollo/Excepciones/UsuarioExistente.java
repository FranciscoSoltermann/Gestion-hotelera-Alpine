package org.TPDesarrollo.Excepciones;


public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente(String message) {
        super(message);
    }
}