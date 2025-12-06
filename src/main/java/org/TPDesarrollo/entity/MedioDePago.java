package org.TPDesarrollo.entity;

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
public abstract class MedioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medio_pago")
    private Integer id;
}