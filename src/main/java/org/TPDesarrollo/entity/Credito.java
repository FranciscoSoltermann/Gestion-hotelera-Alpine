package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.RedDePago;

@Entity
@Table(name = "credito", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Credito extends MedioDePago {

    @Column(name = "numero_tarjeta")
    private int numeroTarjeta;

    @Column(name = "nombre_titular")
    private String nombreTitular;

    @Column(name = "fecha_vencimiento")
    private String fechaVencimiento;

    @Column(name = "codigo_seguridad")
    private int codigoSeguridad;

    @Column(name = "cuotas")
    private int cuotas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RedDePago redDePago;
}