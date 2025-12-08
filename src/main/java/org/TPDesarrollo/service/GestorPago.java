package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.PagoDTO;
import org.TPDesarrollo.entity.Factura;

import java.util.List;

public interface GestorPago {

    public Factura registrarPago(PagoDTO dto);

    List<Factura> obtenerPendientesConSaldo(String habitacion);
}