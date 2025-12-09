package org.TPDesarrollo.entity.habitaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.entity.Habitacion;

/**
 * Clase que representa una habitación doble estándar.
 * Hereda de la clase Habitacion.
 * Esta clase está mapeada a la tabla "doble_estandar" en el esquema "pruebabdd".
 * No contiene atributos adicionales, ya que hereda todos los atributos de la clase base Habitacion.
 */
@Entity
@Table(name = "doble_estandar", schema = "pruebabdd")
@Schema(description = "Entidad que representa una Habitación de tipo Doble Estándar. Hereda todos los atributos y lógica de la clase base Habitacion.")
public class DobleEstandar extends Habitacion {
}