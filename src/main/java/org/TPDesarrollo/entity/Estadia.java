package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Entidad que representa una estadía en el hotel.
 * Registra el período de ocupación real de una habitación por parte de un huésped (Check-in a Check-out).
 */
@Entity
@Table(name = "estadia", schema = "pruebabdd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad central que registra el período de ocupación real de una habitación por parte de un huésped (Check-in a Check-out).")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadia")
    @Schema(description = "ID único de la estadía.", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private int idestadia;

    // NOTA: Estos campos String podrían ser redundantes si se usan los campos LocalDateTime
    @Column(name = "fecha_ingreso")
    @Schema(description = "Fecha de ingreso (string, solo día).", example = "2025-12-05")
    private String fechaIngreso;

    @Column(name = "fecha_egreso")
    @Schema(description = "Fecha de egreso (string, solo día).", example = "2025-12-10")
    private String fechaEgreso;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora exacta del Check-in.", example = "2025-12-05T14:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaHoraIngreso;

    @Schema(description = "Fecha y hora exacta del Check-out.", example = "2025-12-10T10:00:00", nullable = true)
    private LocalDateTime fechaHoraEgreso;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    @Schema(description = "Huésped principal responsable de la estadía.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Huesped huesped;

    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    @Schema(description = "Habitación ocupada durante la estadía.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    @Schema(description = "Reserva asociada que originó la estadía (si aplica).", requiredMode = Schema.RequiredMode.REQUIRED)
    private Reserva reserva;

    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Lista de consumos asociados a esta estadía.", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Consumo> itemsConsumo;
}