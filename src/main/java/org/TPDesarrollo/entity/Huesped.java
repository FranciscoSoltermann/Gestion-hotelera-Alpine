package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.RazonSocial;

import java.time.LocalDate;

@Entity
@Table(name = "huesped", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Huesped extends Persona {

    @Column(unique = true)
    private String cuit;

    @Column(name = "ocupacion")
    private String ocupacion;

    @Column(name = "nacionalidad")
    private String nacionalidad;

    @Column(name = "fecha_nac")
    private LocalDate fechaNacimiento;

    @Column(name = "pos_iva")
    @Enumerated(EnumType.STRING)
    private RazonSocial posicionIVA;

    @Column(name = "email")
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;
}