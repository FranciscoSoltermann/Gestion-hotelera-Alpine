package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ResumenFacturacionDTO;
import org.TPDesarrollo.dto.SolicitudFacturaDTO;
import org.TPDesarrollo.dto.SolicitudNotaCreditoDTO;
import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.entity.NotaCredito;

import java.time.LocalTime;
import java.util.List;
/**
 * Interfaz para la gestión de facturas y notas de crédito.
 */
public interface GestorFactura {
    /**
     * Busca una estadía para facturar basada en el número de habitación y la hora de salida.
     * @param numeroHabitacion El número de la habitación.
     * @param horaSalida La hora de salida.
     * @return Un ResumenFacturacionDTO con los detalles de la estadía para facturar.
     */
    ResumenFacturacionDTO buscarEstadiaParaFacturar(String numeroHabitacion, LocalTime horaSalida);
    /**
     * Genera una factura basada en la solicitud proporcionada.
     * @param solicitud La solicitud de factura.
     * @return La factura generada.
     */
    Factura generarFactura(SolicitudFacturaDTO solicitud);
    /**
     * Busca facturas asociadas a un cliente basado en su documento (DNI o CUIT).
     * @param documento El documento del cliente.
     * @return Una lista de facturas asociadas al cliente.
     */
    List<Factura> buscarFacturasPorCliente(String documento);
    /**
     * Genera una nota de crédito basada en la solicitud proporcionada.
     * @param solicitud La solicitud de nota de crédito.
     * @return La nota de crédito generada.
     */
    NotaCredito generarNotaCredito(SolicitudNotaCreditoDTO solicitud);
}