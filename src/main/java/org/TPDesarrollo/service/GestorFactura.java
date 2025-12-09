package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ResumenFacturacionDTO;
import org.TPDesarrollo.dto.SolicitudFacturaDTO;
import org.TPDesarrollo.dto.SolicitudNotaCreditoDTO;
import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.entity.NotaCredito;

import java.time.LocalTime;
import java.util.List;

public interface GestorFactura {

    ResumenFacturacionDTO buscarEstadiaParaFacturar(String numeroHabitacion, LocalTime horaSalida);

    Factura generarFactura(SolicitudFacturaDTO solicitud);

    List<Factura> buscarFacturasPorCliente(String documento);

    NotaCredito generarNotaCredito(SolicitudNotaCreditoDTO solicitud);
}