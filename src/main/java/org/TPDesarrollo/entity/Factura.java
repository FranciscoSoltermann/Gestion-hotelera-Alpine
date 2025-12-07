package org.TPDesarrollo.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.TPDesarrollo.enums.EstadoFactura;
import org.TPDesarrollo.enums.TipoFactura;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_factura", length = 10)
    private EstadoFactura estado;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura", length = 1)
    private TipoFactura tipoFactura;

    @Column(nullable = false)
    private Float valorEstadia;

    @Column(nullable = false)
    private Float montoTotal;

    @ManyToOne
    @JoinColumn(name = "id_responsable_de_pago", nullable = false)
    private ResponsableDePago responsableDePago;

    @ManyToOne
    @JoinColumn(name = "id_estadia", nullable = false)
    private Estadia estadia;
}