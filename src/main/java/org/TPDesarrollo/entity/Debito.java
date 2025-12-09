package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.TPDesarrollo.enums.RedDePago;
/**
 * Entidad que representa un pago realizado mediante tarjeta de débito.
 * Hereda campos comunes de MedioDePago.
 */
@Entity
@Table(name = "debito", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Entidad que representa un pago realizado mediante tarjeta de débito. Hereda campos comunes de MedioDePago.")
public class Debito extends MedioDePago {

    @Column(name = "numero_tarjeta")
    @Schema(description = "Número de la tarjeta de débito.", example = "1234") // Nota: Se espera un int. Si fuera String se vería el número completo.
    private int numeroTarjeta;

    @Column(name = "nombre_titular")
    @Schema(description = "Nombre del titular tal como aparece en la tarjeta.", example = "CARLOS GUTIERREZ")
    private String nombreTitular;

    @Column(name = "fecha_vencimiento")
    @Schema(description = "Fecha de vencimiento de la tarjeta (ej. MM/YY).", example = "05/29")
    private String fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Red o marca de la tarjeta (ej: VISA_DEBIT, MASTER_DEBIT).", example = "VISA_DEBIT")
    private RedDePago redDePago;
}