package org.TPDesarrollo.dto;

import lombok.Data;
import org.TPDesarrollo.enums.TipoMoneda;
import java.time.LocalDate;

@Data
public class PagoDTO {
    private Long idFactura;
    private String tipoMedioPago;
    private Float monto;
    private Float cotizacion;
    private TipoMoneda moneda;

    // Datos específicos de Tarjeta
    private String numeroTarjeta;
    private String nombreTarjeta;

    // Datos específicos de Cheque
    private String nroCheque;
    private String banco;
    private String plaza;
    private LocalDate fechaCobro;
}