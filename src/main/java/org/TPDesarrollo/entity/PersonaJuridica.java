package org.TPDesarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "persona_juridica", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PersonaJuridica extends ResponsableDePago {

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "cuit", unique = true)
    private String cuit;
}