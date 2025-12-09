package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entidad de catálogo que representa un Banco.
 * Utilizado para registrar pagos con cheque.
 */
@Entity
@Table(name = "banco", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad de catálogo que representa un Banco, utilizado para registrar pagos con cheque.")
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_banco")
    @Schema(description = "ID único del banco.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int idBanco;

    @Schema(description = "Nombre oficial del banco.", example = "BANCO DE GALICIA")
    @Column(name = "nombre")
    private String nombre;
}