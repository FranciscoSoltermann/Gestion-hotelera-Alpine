package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "responsable_de_pago", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class ResponsableDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsable")
    private Integer id;
}