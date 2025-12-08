package org.TPDesarrollo.enums;

/**
 * Define los posibles estados de una Factura dentro del sistema.
 */
public enum EstadoFactura {
    /**
     * La factura ha sido emitida pero aún no se ha liquidado completamente.
     */
    PENDIENTE,

    /**
     * La factura ha sido liquidada en su totalidad con uno o más pagos.
     */
    PAGADA
}