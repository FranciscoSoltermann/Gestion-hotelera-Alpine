package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "responsable_pago", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Clase base abstracta que define los atributos comunes para toda entidad (Persona Física o Jurídica) responsable de abonar una Factura.")
public abstract class ResponsableDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsable")
    @Schema(description = "ID único del responsable de pago.", example = "20", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion", referencedColumnName = "id_direccion")
    @Schema(description = "Dirección fiscal o de contacto del responsable de pago.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Direccion direccion;

    @Column(name = "telefono")
    @Schema(description = "Número de teléfono de contacto.", example = "3415550000")
    private String telefono;

}