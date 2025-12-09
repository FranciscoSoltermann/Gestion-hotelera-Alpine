package org.TPDesarrollo.entity.habitaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.entity.Habitacion;

/**
 * Clase que representa una habitación doble superior en el sistema de gestión hotelera.
 * Hereda de la clase Habitacion.
 * Esta clase está mapeada a la tabla "doble_superior" en el esquema "pruebabdd" de la base de datos.
 * No contiene atributos adicionales, pero puede ser extendida en el futuro para incluir
 * características específicas de las habitaciones dobles superiores.
 */
@Entity
@Table(name = "doble_superior", schema = "pruebabdd")
@Schema(description = "Entidad que representa una Habitación de tipo Doble Superior. Hereda todos los atributos y lógica de la clase base Habitacion.")
public class DobleSuperior extends Habitacion {
}