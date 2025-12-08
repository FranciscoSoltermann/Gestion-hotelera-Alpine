package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ResumenFacturacionDTO;
import org.TPDesarrollo.dto.SolicitudFacturaDTO;
import org.TPDesarrollo.entity.Factura;

import java.time.LocalTime;

public interface GestorFactura {

    ResumenFacturacionDTO buscarEstadiaParaFacturar(String numeroHabitacion, LocalTime horaSalida);

    Factura generarFactura(SolicitudFacturaDTO solicitud);
}