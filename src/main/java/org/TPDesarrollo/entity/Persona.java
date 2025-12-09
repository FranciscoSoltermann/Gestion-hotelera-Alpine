package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.TipoDocumento;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "persona", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Clase base abstracta que define los atributos comunes a cualquier persona (Huésped, Empleado, etc.). Utiliza la estrategia de herencia JOINED.")
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    @Schema(description = "ID único de la persona.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Nombre de pila de la persona.", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Apellido de la persona.", example = "Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @Column(nullable = false)
    @Schema(description = "Número de teléfono de contacto.", example = "3415551234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefono;

    @Column(nullable = false)
    @Schema(description = "Número de documento de identidad.", example = "30123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doc", nullable = false)
    @Schema(description = "Tipo de documento de identidad (ej: DNI, PASAPORTE).", example = "DNI", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoDocumento tipoDocumento;

}