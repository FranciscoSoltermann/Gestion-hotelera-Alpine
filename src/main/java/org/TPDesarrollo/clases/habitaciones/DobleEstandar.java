package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

/**
 * Clase que representa una habitación doble estándar.
 * Hereda de la clase Habitacion.
 * Esta clase está mapeada a la tabla "doble_estandar" en el esquema "pruebabdd".
 * No contiene atributos adicionales, ya que hereda todos los atributos de la clase base Habitacion.
 */
@Entity
@Table(name = "doble_estandar", schema = "pruebabdd")
public class DobleEstandar extends Habitacion {
}