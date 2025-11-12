package org.TPDesarrollo.Excepciones;

public class DniExistente extends RuntimeException {
    public DniExistente(String message) {
        super(message);
    }
}
