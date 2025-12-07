package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nota_credito", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota_credito")
    private Integer id;

    @Column(name = "monto_total")
    private Double montoTotal;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Factura> facturasCanceladas = new ArrayList<>();
}