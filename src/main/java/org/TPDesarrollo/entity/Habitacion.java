package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.TPDesarrollo.enums.EstadoHabitacion;

import java.time.LocalDate;
/**
 * Clase base que define los atributos comunes de todas las habitaciones.
 * Utiliza la estrategia de herencia JOINED.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "habitacion", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Clase base que define los atributos comunes de todas las habitaciones. Utiliza la estrategia de herencia JOINED.")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    @Schema(description = "ID único de la habitación.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idHabitacion;

    @Column(name = "numero", unique = true)
    @Schema(description = "Número visible de la habitación (debe ser único).", example = "105")
    private String numero;

    @Column(name = "capacidad")
    @Schema(description = "Capacidad máxima de ocupantes.", example = "2")
    private Integer capacidad;

    @Column(name = "estado", length = 255)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de la habitación (ej: OCUPADA, DISPONIBLE, MANTENIMIENTO).", example = "DISPONIBLE")
    private EstadoHabitacion estado;

    @Column(name = "ingreso")
    @Schema(description = "Fecha de ingreso actual (si está ocupada).", example = "2025-12-05")
    private LocalDate ingreso;

    @Column(name = "egreso")
    @Schema(description = "Fecha de egreso esperada (si está ocupada).", example = "2025-12-10")
    private LocalDate egreso;

}