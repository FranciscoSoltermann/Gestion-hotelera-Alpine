package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "estadia", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadia")
    private int idestadia;

    @Column(name = "fecha_ingreso")
    private String fechaIngreso;

    @Column(name = "fecha_egreso")
    private String fechaEgreso;

    @Column(nullable = false)
    private LocalDateTime fechaHoraIngreso;

    private LocalDateTime fechaHoraEgreso;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @ManyToMany
    @JoinTable(
            name = "estadias_items_consumo",
            joinColumns = @JoinColumn(name = "estadia_id"),
            inverseJoinColumns = @JoinColumn(name = "item_consumo_id")
    )
    private List<Consumo> itemsConsumo;
}