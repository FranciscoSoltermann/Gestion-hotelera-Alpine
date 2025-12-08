package org.TPDesarrollo.dto;

import lombok.Data;
import org.TPDesarrollo.enums.TipoFactura;
import java.time.LocalTime;
import java.util.List;

@Data
public class SolicitudFacturaDTO {

    private Long idEstadia;
    private Integer idResponsablePagoSeleccionado;
    private String cuitTercero;
    private TipoFactura tipoFactura;
    private List<ItemFacturableDTO> itemsAFacturar;
    private LocalTime horaSalida;
    private String razonSocialTercero;
}