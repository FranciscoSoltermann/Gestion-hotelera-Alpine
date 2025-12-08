package org.TPDesarrollo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.TPDesarrollo.enums.EstadoHabitacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva", schema = "pruebabdd")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "ingreso")
    private LocalDate ingreso;

    @Column(name = "egreso")
    private LocalDate egreso;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Huesped huesped;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ocupante> ocupantes = new ArrayList<>();

    public void agregarOcupante(Ocupante ocupante) {
        ocupantes.add(ocupante);
        ocupante.setReserva(this);
    }

    public EstadoHabitacion getEstadoHabitacion() {
        try {
            return EstadoHabitacion.valueOf(this.estado);
        } catch (Exception e) {
            return EstadoHabitacion.DISPONIBLE;
        }
    }
}