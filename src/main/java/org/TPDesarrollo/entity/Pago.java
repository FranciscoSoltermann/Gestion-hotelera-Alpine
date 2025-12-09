package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.TPDesarrollo.enums.TipoMoneda;

import java.time.LocalDate;

@Entity
@Table(name = "pago", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que registra una transacción de cobro, vinculando una factura con un método de pago y especificando la moneda.")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    @Schema(description = "ID único del registro de pago.", example = "2001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "fecha_cobro", nullable = false)
    @Schema(description = "Fecha en la que se realizó el cobro.", example = "2025-12-08", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaCobro;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 10)
    @Schema(description = "Tipo de moneda utilizada en el pago (ej: ARS, USD).", example = "ARS", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoMoneda moneda;

    @Column(name = "importe", nullable = false)
    @Schema(description = "Monto pagado en la moneda especificada.", example = "58500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double importe;

    @Column(name = "cotizacion")
    @Schema(description = "Tasa de cambio aplicada si el pago se realizó en moneda extranjera. Es 1.0 si es moneda local.", example = "1000.00", nullable = true)
    private Double cotizacion;

    @OneToOne
    @JoinColumn(name = "id_factura", nullable = false)
    @Schema(description = "Referencia a la factura que está siendo cubierta por este pago.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Factura factura;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medio_de_pago", nullable = false, unique = true)
    @Schema(description = "Referencia al método de pago específico (Efectivo, Crédito, Cheque) utilizado en la transacción.", requiredMode = Schema.RequiredMode.REQUIRED)
    private MedioDePago medioDePago;
}