package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Entidad que representa una Nota de Crédito emitida para anular o compensar facturas existentes.
 */
@Entity
@Table(name = "nota_credito", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa una Nota de Crédito emitida para anular o compensar facturas existentes.")
public class NotaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota_credito")
    @Schema(description = "ID único de la nota de crédito.", example = "50", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "monto_total")
    @Schema(description = "Monto total de la nota de crédito (valor a favor del cliente).", example = "5000.00")
    private Double montoTotal;

    @Column(name = "fecha_emision")
    @Schema(description = "Fecha en la que se emitió la nota de crédito.", example = "2025-12-10")
    private LocalDate fechaEmision;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @Schema(description = "Lista de facturas que son compensadas o canceladas total/parcialmente por esta nota de crédito.", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Factura> facturasCanceladas = new ArrayList<>();
}