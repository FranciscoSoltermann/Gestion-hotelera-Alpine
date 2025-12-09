package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.RedDePago;
/**
 * Entidad que representa un pago realizado mediante tarjeta de crédito.
 * Hereda campos comunes de MedioDePago.
 */
@Entity
@Table(name = "credito", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Entidad que representa un pago realizado mediante tarjeta de crédito. Hereda campos comunes de MedioDePago.")
public class Credito extends MedioDePago {

    @Column(name = "numero_tarjeta")
    @Schema(description = "Número de la tarjeta de crédito.", example = "1234") // Nota: Se espera un int. Si fuera String se vería el número completo.
    private int numeroTarjeta;

    @Column(name = "nombre_titular")
    @Schema(description = "Nombre del titular tal como aparece en la tarjeta.", example = "PEDRO PICAPIEDRA")
    private String nombreTitular;

    @Column(name = "fecha_vencimiento")
    @Schema(description = "Fecha de vencimiento de la tarjeta (ej. MM/YY).", example = "12/28")
    private String fechaVencimiento;

    @Column(name = "cuotas")
    @Schema(description = "Número de cuotas seleccionadas para el pago.", example = "3")
    private int cuotas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Red o marca de la tarjeta (ej: VISA, MASTERCARD).", example = "VISA")
    private RedDePago redDePago;
}