package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que almacena los datos de una dirección física, utilizada por Huéspedes y Responsables de Pago.")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    @Schema(description = "ID único de la dirección.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "piso")
    @Schema(description = "Piso del domicilio (opcional).", example = "3")
    private String piso;

    @Column(name = "departamento")
    @Schema(description = "Departamento o número de oficina (opcional).", example = "A")
    private String departamento;

    @Column(name = "numero")
    @Schema(description = "Número de la calle o casa.", example = "742")
    private String numero;

    @Column(name = "calle")
    @Schema(description = "Nombre de la calle.", example = "Av. Santa Fe")
    private String calle;

    @Column(name = "cod_postal")
    @Schema(description = "Código Postal.", example = "2000")
    private String codigoPostal;

    @Column(name = "pais")
    @Schema(description = "País del domicilio.", example = "Argentina")
    private String pais;

    @Column(name = "provincia")
    @Schema(description = "Provincia o Estado.", example = "Santa Fe")
    private String provincia;

    @Column(name = "localidad")
    @Schema(description = "Localidad o Ciudad.", example = "Rosario")
    private String localidad;
}