package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ocupante", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Ocupante extends Persona {

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;
}