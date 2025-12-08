package org.TPDesarrollo.enums;

/**
 * Define los diferentes tipos de documentos de identificación que pueden ser utilizados
 * para registrar a una persona (Huésped, Ocupante, etc.) en el sistema.
 */
public enum TipoDocumento {
    /**
     * Documento Nacional de Identidad (Argentina).
     */
    DNI,

    /**
     * Documento oficial emitido por un gobierno nacional para viajes internacionales.
     */
    PASAPORTE,

    /**
     * Libreta Cívica (Tipo de documento histórico argentino, utilizado en el pasado).
     */
    LC,

    /**
     * Libreta de Enrolamiento (Tipo de documento histórico argentino, utilizado en el pasado).
     */
    LE,

    /**
     * Representa cualquier otro tipo de identificación no contemplado específicamente.
     */
    OTRO
}