package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

@Entity
@Table(name = "ind_estandar") // Coincide con tu diagrama ER
public class IndividualEstandar extends Habitacion {
    // Hereda todo de Habitacion
}