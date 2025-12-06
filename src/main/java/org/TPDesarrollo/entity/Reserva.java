package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva", schema = "pruebabdd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id;

    @Column(name = "ingreso")
    private LocalDate ingreso;

    @Column(name = "egreso")
    private LocalDate egreso;

    @Column(name = "id_persona")
    private Integer idPersona;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ocupante> ocupantes = new ArrayList<>();

    // --- MÉTODOS DE NEGOCIO ---
    public void agregarOcupante(Ocupante ocupante) {
        if (this.ocupantes == null) {
            this.ocupantes = new ArrayList<>();
        }
        this.ocupantes.add(ocupante);
        ocupante.setReserva(this);
    }
}