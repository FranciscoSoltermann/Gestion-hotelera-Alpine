package org.TPDesarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "persona_fisica", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PersonaFisica extends ResponsableDePago {

    private String nombre;

    private String apellido;

    @Column(unique = true)
    private String dni;

    @Column(unique = true)
    private String cuit;
}