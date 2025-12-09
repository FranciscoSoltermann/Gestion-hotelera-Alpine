package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.TPDesarrollo.enums.EstadoHabitacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Entidad que registra la intención de un huésped de ocupar una habitación en un período futuro (o actual).
 */
@Entity
@Table(name = "reserva", schema = "pruebabdd")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que registra la intención de un huésped de ocupar una habitación en un período futuro (o actual).")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    @Schema(description = "ID único de la reserva.", example = "500", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idReserva;

    @Column(name = "ingreso")
    @Schema(description = "Fecha de inicio de la reserva (Check-in esperado).", example = "2026-03-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate ingreso;

    @Column(name = "egreso")
    @Schema(description = "Fecha de fin de la reserva (Check-out esperado).", example = "2026-03-10", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate egreso;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    @Schema(description = "Huésped principal que realiza la reserva.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Huesped huesped;

    @Column(name = "estado")
    @Schema(description = "Estado actual de la reserva (ej: PENDIENTE, CONFIRMADA, CANCELADA, EXPIRADA).", example = "CONFIRMADA")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    @Schema(description = "Habitación reservada (referencia a la clase base Habitacion).", requiredMode = Schema.RequiredMode.REQUIRED)
    private Habitacion habitacion;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de ocupantes asociados a esta reserva.", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Ocupante> ocupantes = new ArrayList<>();

    // --- Métodos de Lógica ---

    @Schema(description = "Método de conveniencia para añadir un ocupante a la lista.")
    public void agregarOcupante(Ocupante ocupante) {
        ocupantes.add(ocupante);
        ocupante.setReserva(this);
    }

    @Schema(description = "Convierte el estado de la reserva a un tipo de EstadoHabitacion (si es válido). Campo de solo lectura.", accessMode = Schema.AccessMode.READ_ONLY)
    public EstadoHabitacion getEstadoHabitacion() {
        try {
            return EstadoHabitacion.valueOf(this.estado);
        } catch (Exception e) {
            return EstadoHabitacion.DISPONIBLE;
        }
    }
}