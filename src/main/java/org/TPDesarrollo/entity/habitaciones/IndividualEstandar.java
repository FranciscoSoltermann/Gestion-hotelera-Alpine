package org.TPDesarrollo.entity.habitaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.entity.Habitacion;

/**
 * Clase que representa una habitación individual estándar.
 * Hereda de la clase Habitacion.
 * Esta clase está mapeada a la tabla "ind_estandar" en el esquema "pruebabdd".
 * No contiene atributos adicionales, ya que hereda todos los atributos de la clase Habitacion.
 */
@Entity
@Table(name = "ind_estandar", schema = "pruebabdd")
@Schema(description = "Entidad que representa una Habitación de tipo Individual Estándar. Hereda todos los atributos y lógica de la clase base Habitacion.")
public class IndividualEstandar extends Habitacion {
    // Hereda todo de Habitacion
}