package org.TPDesarrollo.clases.habitaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.TPDesarrollo.clases.Habitacion;

@Entity
@Table(name = "superior_family") // Ajustado al diagrama ER (dice 'Superior_Family')
public class SuperiorFamilyPlan extends Habitacion {
}