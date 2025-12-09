package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
/**
 * Entidad que representa un pago realizado mediante cheque bancario.
 * Hereda campos comunes de MedioDePago.
 */
@Entity
@Table(name = "cheque", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Entidad que representa un pago realizado mediante cheque bancario. Hereda campos comunes de MedioDePago.")
public class Cheque extends MedioDePago {

    @Column(name = "numero_cheque")
    @Schema(description = "Número único de identificación del cheque.", example = "12345678")
    private int numeroCheque;

    @Column(name = "fecha_cheque")
    @Schema(description = "Fecha de emisión o fecha de cobro del cheque (Formato ISO).", example = "2026-01-01")
    private LocalDate fechaCheque;

    @Column(name = "plaza")
    @Schema(description = "Lugar o plaza de emisión del cheque.", example = "ROSARIO")
    private String Plaza;

    @ManyToOne
    @JoinColumn(name = "id_banco")
    @Schema(description = "Referencia al banco emisor asociado.")
    private Banco banco;
}