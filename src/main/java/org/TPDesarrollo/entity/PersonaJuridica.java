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
@Table(name = "persona_juridica", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Entidad que representa a una empresa u organización (persona jurídica) responsable de pagar una factura, heredando la información de ResponsableDePago.")
public class PersonaJuridica extends ResponsableDePago {

    @Column(name = "razon_social")
    @Schema(description = "Nombre legal de la empresa o razón social.", example = "Hotel Premier S.A.")
    private String razonSocial;

    @Column(name = "cuit", unique = true)
    @Schema(description = "Clave Única de Identificación Tributaria (CUIT) de la empresa. Debe ser único.", example = "30556677889")
    private String cuit;
}