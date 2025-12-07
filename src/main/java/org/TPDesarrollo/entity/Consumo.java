package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumo", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    private int idConsumo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_unitario")
    private Float precioUnitario;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_consumo")
    private LocalDateTime fechaConsumo;

    @ManyToOne
    @JoinColumn(name = "id_estadia")
    private Estadia estadia;

    public Float getMontoTotal() {
        if (precioUnitario == null || cantidad == null) return 0f;
        return precioUnitario * cantidad;
    }
}