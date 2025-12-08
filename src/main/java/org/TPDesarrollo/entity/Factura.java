package org.TPDesarrollo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.enums.EstadoFactura;
import org.TPDesarrollo.enums.TipoFactura;

@Entity
@Table(name = "factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa una factura emitida, registrando el monto total y el responsable de pago.")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la factura.", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_factura", length = 10)
    @Schema(description = "Estado actual de la factura (ej: PAGADA, PENDIENTE).", example = "PENDIENTE", requiredMode = Schema.RequiredMode.REQUIRED)
    private EstadoFactura estado;

    @Column(nullable = false)
    @Schema(description = "Fecha en la que se emitió la factura.", example = "2025-12-08", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaEmision;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura", length = 1)
    @Schema(description = "Tipo de factura emitida (ej: A, B).", example = "B", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoFactura tipoFactura;

    @Column(nullable = false)
    @Schema(description = "Monto total correspondiente a los días de estadía (sin consumos adicionales).", example = "45000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private float valorEstadia;

    @Column(nullable = false)
    @Schema(description = "Monto total final a pagar (Estadía + Consumos + Impuestos).", example = "58500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private float montoTotal;

    @ManyToOne
    @JoinColumn(name = "id_responsable_de_pago", nullable = false)
    @JsonIgnore
    @Schema(description = "Referencia al Huésped o Empresa responsable de liquidar la factura.", requiredMode = Schema.RequiredMode.REQUIRED)
    private ResponsableDePago responsableDePago;
    @Transient
    private String responsableNombre;

    @Transient
    private String responsableDoc;
    @ManyToOne
    @JoinColumn(name = "id_estadia", nullable = false)
    @Schema(description = "Referencia a la Estadía a la que corresponde esta factura.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Estadia estadia;

    @Transient
    @Schema(description = "Saldo pendiente de pago (calculado: Monto Total - Pagos realizados). Campo de solo lectura.", example = "8500.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Double saldoPendiente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FacturaDetalle> detalles = new ArrayList<>();
}