package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "medio_de_pago", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Clase base abstracta para todos los métodos de pago (Efectivo, Cheque, Tarjeta). Usa la estrategia de herencia JOINED.")
public abstract class MedioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medio_pago")
    @Schema(description = "ID único del medio de pago.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

}