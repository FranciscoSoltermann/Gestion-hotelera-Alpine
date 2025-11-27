package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

@Entity
@Table(name = "suite_doble", schema = "pruebabdd")
public class SuiteDoble extends Habitacion {
}