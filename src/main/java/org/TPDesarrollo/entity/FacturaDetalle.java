package org.TPDesarrollo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entidad que representa una línea de detalle o ítem dentro de una Factura.
 */
@Entity
@Table(name = "factura_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa una línea de detalle o ítem dentro de una Factura.")
public class FacturaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del detalle.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Descripción del cargo (ej: Alojamiento, Consumo Minibar).", example = "Alojamiento Habitación 105", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    @Column(length = 50)
    @Schema(description = "Unidad de medida (ej: días, unidades).", example = "días")
    private String medida;

    @Column(nullable = false)
    @Schema(description = "Cantidad del ítem facturado.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Precio del ítem por unidad.", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Float precioUnitario;

    @Column(nullable = false)
    @Schema(description = "Subtotal calculado (Cantidad * Precio Unitario).", example = "45000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Float subtotal;

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    @Schema(description = "Referencia a la factura a la que pertenece este detalle.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonIgnore // <--- AGREGA ESTO AQUÍ
    private Factura factura;

    @Column(name = "es_estadia")
    @Schema(description = "Indica si el detalle corresponde a la estadía (true) o a otros consumos (false).", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean esEstadia;
}