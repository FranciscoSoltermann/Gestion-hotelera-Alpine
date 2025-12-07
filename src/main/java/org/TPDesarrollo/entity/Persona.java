package org.TPDesarrollo.entity;

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
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doc", nullable = false)
    private TipoDocumento tipoDocumento;

}