package org.TPDesarrollo.dto;

import lombok.Builder;
import lombok.Data;
import org.TPDesarrollo.enums.TipoFactura;
import java.util.List;

@Data
@Builder
public class ResumenFacturacionDTO {
    private Long idEstadia;
    private String numeroHabitacion;
    private List<ItemFacturableDTO> items; // Estadía y consumos
    private List<ResponsableDePagoDTO> posiblesResponsables; // Ocupantes
    private Double montoTotal;
    private TipoFactura tipoFacturaSugerido; // A o B
}