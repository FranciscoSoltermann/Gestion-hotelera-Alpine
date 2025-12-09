package org.TPDesarrollo.enums;

/**
 * Define la posición fiscal (razón social) de una entidad o persona
 * frente al Impuesto al Valor Agregado (IVA) en Argentina.
 * Utilizado para la emisión de facturas.
 */
public enum RazonSocial {
    /**
     * Contribuyente que emite facturas A (o B/C si corresponde) y está obligado a liquidar IVA.
     */
    Responsable_Inscripto,

    /**
     * Pequeño contribuyente con un régimen simplificado, emite facturas C.
     */
    Monotributista,

    /**
     * Cliente o consumidor final que no discrimina impuestos en la factura, recibe facturas B o C.
     */
    Consumidor_Final
}