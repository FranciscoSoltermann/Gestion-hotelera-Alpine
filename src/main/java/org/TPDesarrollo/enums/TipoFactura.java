package org.TPDesarrollo.enums;

/**
 * Define los tipos de comprobantes fiscales (facturas) utilizados en la contabilidad.
 */
public enum TipoFactura {
    /**
     * Comprobante utilizado principalmente para transacciones entre Responsables Inscriptos (B2B),
     * donde se discrimina el Impuesto al Valor Agregado (IVA).
     */
    A,

    /**
     * Comprobante utilizado en transacciones con Consumidores Finales, Exentos o Monotributistas,
     * donde el IVA no se discrimina en el cuerpo del documento.
     */
    B
}