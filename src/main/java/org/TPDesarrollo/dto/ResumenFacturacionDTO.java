package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.TPDesarrollo.enums.TipoFactura;
import java.util.List;

/**
 * DTO de RESPUESTA que resume la información de costos y responsables de pago de una estadía activa.
 * Incluye detalles como los ítems facturables, posibles responsables de pago,
 * el monto total acumulado y el tipo de factura sugerido.
 */
@Data
@Builder
@Schema(description = "DTO de RESPUESTA que resume la información de costos y responsables de pago de una estadía activa.")
public class ResumenFacturacionDTO {

    @Schema(description = "ID de la estadía activa.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idEstadia;

    @Schema(description = "Número de habitación asociado a la estadía.", example = "105", accessMode = Schema.AccessMode.READ_ONLY)
    private String numeroHabitacion;

    @Schema(description = "Lista de cargos a facturar (estadía, consumos, daños).", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ItemFacturableDTO> items; // Estadía y consumos

    @Schema(description = "Lista de huéspedes y terceros que pueden asumir la responsabilidad del pago.", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ResponsableDePagoDTO> posiblesResponsables; // Ocupantes

    @Schema(description = "Monto total acumulado a facturar, incluyendo cargos por estadía y consumos.", example = "48500.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Double montoTotal;

    @Schema(description = "Tipo de factura sugerido basado en la posición fiscal de los responsables (ej: B si es Consumidor Final).", example = "B", accessMode = Schema.AccessMode.READ_ONLY)
    private TipoFactura tipoFacturaSugerido; // A o B
}