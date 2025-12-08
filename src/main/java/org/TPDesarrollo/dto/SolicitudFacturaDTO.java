package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.enums.TipoFactura;

import java.time.LocalTime;
import java.util.List;

/**
 * DTO de SOLICITUD para finalizar el proceso de facturación y crear el comprobante.
 * Contiene la información necesaria para emitir la factura,
 * incluyendo el ID de la estadía, responsable de pago,
 * tipo de factura, items a facturar y hora de salida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de SOLICITUD para finalizar el proceso de facturación y crear el comprobante.")
public class SolicitudFacturaDTO {

    @Schema(description = "ID de la estadía activa que está siendo facturada.", example = "10")
    @NotNull(message = "El ID de la estadía es obligatorio.")
    @Positive(message = "El ID de la estadía debe ser un número positivo.")
    private Long idEstadia;

    @Schema(description = "ID del huésped o responsable de pago seleccionado en el sistema (si no es tercero).", example = "42", nullable = true)
    private Integer idResponsablePagoSeleccionado;

    @Schema(description = "CUIT del tercero (empresa) si se factura a alguien que no es huésped (Solo si idResponsablePagoSeleccionado es nulo).", example = "20123456789", nullable = true)
    @Pattern(regexp = "^[0-9]{11}$", message = "El CUIT debe tener exactamente 11 números.")
    private String cuitTercero;

    @Schema(description = "Razón social del tercero (empresa) si se factura a alguien que no es huésped.", example = "PEPE SA", nullable = true)
    @Size(max = 100, message = "La razón social no puede exceder los 100 caracteres.")
    private String razonSocialTercero;

    @Schema(description = "Tipo de factura a emitir (A o B).", example = "B")
    @NotNull(message = "El tipo de factura es obligatorio.")
    private TipoFactura tipoFactura;

    @Schema(description = "Lista de items seleccionados para incluir en la factura.", required = true)
    @NotEmpty(message = "Debe seleccionar al menos un item para facturar.")
    @Valid // Valida cada ItemFacturableDTO en la lista
    private List<ItemFacturableDTO> itemsAFacturar;

    @Schema(description = "Hora de salida utilizada para calcular el último cargo por estadía (Formato HH:mm).", example = "10:30")
    @NotNull(message = "La hora de salida es obligatoria.")
    private LocalTime horaSalida;
}