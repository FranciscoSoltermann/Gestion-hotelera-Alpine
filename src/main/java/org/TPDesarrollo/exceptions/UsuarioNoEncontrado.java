package org.TPDesarrollo.exceptions;

/**
 * Excepción lanzada cuando un usuario no es encontrado en el sistema.
 * Puede ser utilizada para indicar que el usuario buscado no existe o no está registrado.
 * Extiende de la clase Exception para permitir su manejo en bloques try-catch.
 */
public class UsuarioNoEncontrado extends Exception {
    /**
     * Constructor de la excepción UsuarioNoEncontrado.
     */
    public UsuarioNoEncontrado(String message) {
        super(message);
    }
}
