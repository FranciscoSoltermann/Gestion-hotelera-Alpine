package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa a un individuo (persona física) responsable de pagar una factura, heredando la información de ResponsableDePago.")
public class PersonaFisica extends ResponsableDePago {

    @Schema(description = "Nombre de pila del responsable.", example = "Mario")
    private String nombre;

    @Schema(description = "Apellido del responsable.", example = "Gomez")
    private String apellido;

    @Column(unique = true)
    @Schema(description = "Número de Documento Nacional de Identidad (DNI). Debe ser único.", example = "30123456")
    private String dni;

    @Column(unique = true)
    @Schema(description = "Clave Única de Identificación Tributaria (CUIT). Debe ser único.", example = "20301234567")
    private String cuit;
}