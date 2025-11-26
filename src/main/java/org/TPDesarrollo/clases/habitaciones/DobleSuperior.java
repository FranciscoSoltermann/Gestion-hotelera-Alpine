package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

@Entity
@Table(name = "doble_superior", schema = "pruebabdd")
public class DobleSuperior extends Habitacion {
}