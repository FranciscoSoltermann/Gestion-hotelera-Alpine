package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.PagoDTO;
import org.TPDesarrollo.entity.Factura;

import java.util.List;
/**
 * Interfaz para la gestión de pagos.
 */
public interface GestorPago {
    /**
     * Registra un pago basado en el DTO proporcionado.
     *
     * @param dto El objeto PagoDTO que contiene la información del pago a registrar.
     * @return La factura actualizada después de registrar el pago.
     */
    public Factura registrarPago(PagoDTO dto);
    /**
     * Obtiene una lista de facturas pendientes con saldo para una habitación específica.
     *
     * @param habitacion El identificador de la habitación.
     * @return Una lista de facturas pendientes con saldo.
     */
    List<Factura> obtenerPendientesConSaldo(String habitacion);
}