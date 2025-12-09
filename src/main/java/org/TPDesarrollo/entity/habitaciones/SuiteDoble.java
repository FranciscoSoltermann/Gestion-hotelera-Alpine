package org.TPDesarrollo.entity.habitaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.entity.Habitacion;

/**
 * Clase que representa una habitación de tipo Suite Doble.
 * Hereda de la clase Habitacion.
 * Esta clase no añade atributos adicionales, pero puede ser
 * extendida en el futuro para incluir características específicas
 * de una Suite Doble.
 */
@Entity
@Table(name = "suite_doble", schema = "pruebabdd")
@Schema(description = "Entidad que representa una Habitación de tipo Suite Doble. Hereda todos los atributos y lógica de la clase base Habitacion.")
public class SuiteDoble extends Habitacion {
}