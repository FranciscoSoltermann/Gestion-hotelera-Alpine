package org.TPDesarrollo.dto;

import lombok.Data;
import org.TPDesarrollo.enums.TipoFactura;
import java.time.LocalTime;
import java.util.List;

@Data
public class SolicitudFacturaDTO {
    private Long idEstadia;
    private Integer idResponsablePagoSeleccionado;
    private String cuitTercero; // Opcional, si factura a tercero
    private TipoFactura tipoFactura;
    private List<ItemFacturableDTO> itemsAFacturar; // Solo los tildados
    private LocalTime horaSalida; // Ingresada por el actor [cite: 935]
}