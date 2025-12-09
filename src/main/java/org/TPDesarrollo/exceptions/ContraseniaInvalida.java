package org.TPDesarrollo.exceptions;

/**
 * Excepción lanzada cuando una contraseña es inválida.
 * Puede ser utilizada para indicar que la contraseña no cumple con los requisitos
 * de seguridad o formato esperado.
 */
public class ContraseniaInvalida extends Exception {
    /**
     * Constructor de la excepción con un mensaje personalizado.
     */
    public ContraseniaInvalida(String message) {
        super(message);
    }
}
