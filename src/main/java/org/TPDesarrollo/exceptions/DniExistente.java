package org.TPDesarrollo.exceptions;

/**
 * Excepción personalizada que indica que el DNI ya existe en el sistema.
 * Se lanza cuando se intenta registrar un cliente con un DNI que ya está registrado.
 */
public class DniExistente extends RuntimeException {
    public DniExistente(String message) {
        super(message);
    }
}
