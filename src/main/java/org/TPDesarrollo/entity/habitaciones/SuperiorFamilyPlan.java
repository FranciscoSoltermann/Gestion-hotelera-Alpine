package org.TPDesarrollo.entity.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.entity.Habitacion;

/**
 * Clase que representa una habitación de tipo Superior Family Plan.
 * Hereda de la clase Habitacion.
 * Esta clase puede incluir atributos y métodos específicos
 * para las habitaciones de este tipo en el futuro.
 */
@Entity
@Table(name = "superior_family", schema = "pruebabdd")
public class SuperiorFamilyPlan extends Habitacion {

}