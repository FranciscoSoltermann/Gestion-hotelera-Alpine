package org.TPDesarrollo.entity;

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
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer id;

    @Column(name = "fecha_cobro", nullable = false)
    private LocalDate fechaCobro;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 10)
    private TipoMoneda moneda;

    @Column(name = "importe", nullable = false)
    private Double importe;

    @Column(name = "cotizacion")
    private Double cotizacion;

    @OneToOne
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medio_de_pago", nullable = false, unique = true)
    private MedioDePago medioDePago;
}