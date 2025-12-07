package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.enums.EstadoHabitacion;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "habitacion", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Integer idHabitacion;

    @Column(name = "numero", unique = true)
    private String numero;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "estado", length = 255)
    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estado;

    @Column(name = "ingreso")
    private LocalDate ingreso;

    @Column(name = "egreso")
    private LocalDate egreso;

}