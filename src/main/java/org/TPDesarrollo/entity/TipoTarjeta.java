package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entidad de catálogo que define el tipo de tarjeta (ej: Visa, Mastercard, American Express).
 */
@Entity
@Table(name = "tipo_tarjeta", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad de catálogo que define el tipo de tarjeta (ej: Visa, Mastercard, American Express).")
public class TipoTarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del tipo de tarjeta.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int idTipoTarjeta;

    @Column(name = "nombre")
    @Schema(description = "Nombre del tipo de tarjeta.", example = "VISA")
    private String nombre;
}