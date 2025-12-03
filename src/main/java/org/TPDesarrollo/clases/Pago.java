package org.TPDesarrollo.clases;

import java.time.LocalDate;

public class Pago {

    private int idPago;
    private String descripcion;
    private float importe;
    private float montoPagado;
    private String moneda;
    private LocalDate fechaPago;
    private LocalDate plaza;

    private MedioDePago medioDePago;
    private Factura factura;
    private Estadia estadia;
    private Consumo consumo;

    public Pago(MedioDePago medioDePago, Factura factura, Estadia estadia, Consumo consumo) {
        this.medioDePago = medioDePago;
        this.factura = factura;
        this.estadia = estadia;
        this.consumo = consumo;
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }

    public Factura getFactura() {
        return factura;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public Consumo getConsumo() {
        return consumo;
    }

}
