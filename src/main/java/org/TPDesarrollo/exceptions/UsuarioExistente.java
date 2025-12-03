package org.TPDesarrollo.exceptions;

/**
 * Excepción lanzada cuando se intenta registrar un usuario que ya existe.
 * Extiende RuntimeException para indicar un error en tiempo de ejecución.
 * Contiene un constructor que acepta un mensaje de error personalizado.
 */
public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente(String message) {
        super(message);
    }
}