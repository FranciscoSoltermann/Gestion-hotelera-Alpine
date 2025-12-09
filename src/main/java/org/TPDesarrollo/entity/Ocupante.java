package org.TPDesarrollo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * Entidad que representa a un individuo que está ocupando físicamente una habitación.
 * Hereda datos básicos de la clase Persona.
 */
@Entity
@Table(name = "ocupante", schema = "pruebabdd")
@PrimaryKeyJoinColumn(name = "id_persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entidad que representa a un individuo que está ocupando físicamente una habitación, heredando sus datos básicos de Persona.")
public class Ocupante extends Persona {

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    @JsonIgnore
    @Schema(description = "Referencia a la reserva a la que está asociado este ocupante (campo ignorado en serialización JSON para evitar recursión).", accessMode = Schema.AccessMode.READ_ONLY)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    @Schema(description = "Habitación específica que está ocupando este individuo.")
    private Habitacion habitacion;
}