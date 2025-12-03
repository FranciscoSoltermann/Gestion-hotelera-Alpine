package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

/**
 * Clase que representa una habitación de tipo Suite Doble.
 * Hereda de la clase Habitacion.
 * Esta clase no añade atributos adicionales, pero puede ser
 * extendida en el futuro para incluir características específicas
 * de una Suite Doble.
 */
@Entity
@Table(name = "suite_doble", schema = "pruebabdd")
public class SuiteDoble extends Habitacion {
}