package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direccion", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer id;

    @Column(name = "piso")
    private String piso;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "numero")
    private String numero;

    @Column(name = "calle")
    private String calle;

    @Column(name = "cod_postal")
    private String codigoPostal;

    @Column(name = "pais")
    private String pais;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "localidad")
    private String localidad;
}