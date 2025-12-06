package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.EstadoHabitacion;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "habitacion", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Integer id;

    @Column(name = "numero", unique = true)
    private String numero;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoHabitacion estado;

    @Column(name = "ingreso")
    private LocalDate ingreso;

    @Column(name = "egreso")
    private LocalDate egreso;
}