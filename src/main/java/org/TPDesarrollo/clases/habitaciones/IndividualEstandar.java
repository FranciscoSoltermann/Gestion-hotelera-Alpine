package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

/**
 * Clase que representa una habitación individual estándar.
 * Hereda de la clase Habitacion.
 * Esta clase está mapeada a la tabla "ind_estandar" en el esquema "pruebabdd".
 * No contiene atributos adicionales, ya que hereda todos los atributos de la clase Habitacion.
 */
@Entity
@Table(name = "ind_estandar", schema = "pruebabdd")
public class IndividualEstandar extends Habitacion {
    // Hereda todo de Habitacion
}