package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumo", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que registra un cargo o consumo dentro de una estadía (ej: minibar, lavandería).")
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    @Schema(description = "ID único del consumo.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private int idConsumo;

    @Column(name = "descripcion")
    @Schema(description = "Descripción del producto o servicio consumido.", example = "Botella de agua mineral")
    private String descripcion;

    @Column(name = "precio_unitario")
    @Schema(description = "Precio del ítem sin impuestos.", example = "500.00")
    private Float precioUnitario;

    @Column(name = "cantidad")
    @Schema(description = "Cantidad consumida.", example = "2")
    private Integer cantidad;

    @Column(name = "fecha_consumo")
    @Schema(description = "Fecha y hora en que se registró el consumo.", example = "2025-11-20T15:30:00")
    private LocalDateTime fechaConsumo;

    @ManyToOne
    @JoinColumn(name = "id_estadia")
    @JsonIgnore
    @Schema(description = "Referencia a la estadía a la que pertenece este consumo.", accessMode = Schema.AccessMode.READ_ONLY)
    private Estadia estadia;

    @Schema(description = "Monto total calculado (Precio Unitario * Cantidad). Campo de sólo lectura/calculado.", accessMode = Schema.AccessMode.READ_ONLY)
    public Float getMontoTotal() {
        if (precioUnitario == null || cantidad == null) return 0f;
        return precioUnitario * cantidad;
    }
}