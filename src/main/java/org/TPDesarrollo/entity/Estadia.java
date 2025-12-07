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
    @JoinColumn(name = "id_persona", nullable = false)
    private Huesped huesped;
    
    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consumo> itemsConsumo;
}