package org.TPDesarrollo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "efectivo", schema = "pruebabdd")
@Data
@NoArgsConstructor
@SuperBuilder
public class Efectivo extends MedioDePago {

}