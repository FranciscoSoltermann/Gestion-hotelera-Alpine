package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.TPDesarrollo.enums.TipoMoneda;
import java.time.LocalDate;

/**
 * DTO que encapsula la información de un pago para una factura específica.
 * Contiene campos condicionales según el medio de pago.
 */
@Data
@Schema(description = "DTO que encapsula la información de un pago para una factura específica. Contiene campos condicionales según el medio de pago.")
public class PagoDTO {

    @Schema(description = "ID de la factura a la que se aplica el pago.", example = "1023")
    @NotNull(message = "El ID de la factura es obligatorio.")
    @Positive(message = "El ID de la factura debe ser un número positivo.")
    private Long idFactura;

    @Schema(description = "Medio por el cual se realiza el pago (EFECTIVO, TARJETA, CHEQUE, etc.).", example = "TARJETA")
    @NotBlank(message = "El tipo de medio de pago es obligatorio.")
    private String tipoMedioPago;

    @Schema(description = "Monto a aplicar al pago. Debe ser positivo.", example = "50000.00")
    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "0.01", message = "El monto debe ser superior a cero.")
    private Float monto;

    @Schema(description = "Moneda del pago (ej: ARS, USD).", example = "ARS")
    @NotNull(message = "La moneda es obligatoria.")
    private TipoMoneda moneda;

    // La cotización solo es obligatoria si la moneda no es ARS (debe validarse en Service)
    @Schema(description = "Cotización utilizada para la conversión (si la moneda es diferente a ARS).", example = "1000.00")
    @DecimalMin(value = "1.0", message = "La cotización mínima debe ser 1.0.")
    private Float cotizacion;

    @Schema(description = "Número de tarjeta de crédito/débito (Obligatorio si tipoMedioPago=TARJETA).", example = "1234567890123456")
    @Size(max = 16, message = "El número de tarjeta no debe exceder 16 dígitos.")
    @Pattern(regexp = "^[0-9]*$", message = "El número de tarjeta solo puede contener números.")
    private String numeroTarjeta;

    @Schema(description = "Nombre impreso en la tarjeta.", example = "Juan Perez")
    @Size(max = 100, message = "El nombre de la tarjeta es demasiado largo.")
    private String nombreTarjeta;

    @Schema(description = "Número de Cheque (Obligatorio si tipoMedioPago=CHEQUE).", example = "00012345")
    @Size(max = 20, message = "El número de cheque es demasiado largo.")
    private String nroCheque;

    @Schema(description = "Banco emisor del Cheque.", example = "BANCO DE GALICIA")
    @Size(max = 50, message = "El nombre del banco es demasiado largo.")
    private String banco;

    @Schema(description = "Plaza del cheque (localidad de emisión).", example = "Rosario")
    @Size(max = 50, message = "La plaza es demasiado larga.")
    private String plaza;

    @Schema(description = "Fecha de cobro del Cheque (debe ser hoy o en el futuro).", example = "2026-01-15")
    @FutureOrPresent(message = "La fecha de cobro del cheque no puede ser en el pasado.")
    private LocalDate fechaCobro;
}