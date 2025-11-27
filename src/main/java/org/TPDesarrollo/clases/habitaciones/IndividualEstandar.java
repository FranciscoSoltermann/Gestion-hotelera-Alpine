package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

@Entity
@Table(name = "ind_estandar", schema = "pruebabdd")
public class IndividualEstandar extends Habitacion {
    // Hereda todo de Habitacion
}