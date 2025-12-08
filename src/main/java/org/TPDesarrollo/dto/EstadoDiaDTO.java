package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data; // Importamos Lombok Data
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.TPDesarrollo.enums.EstadoHabitacion;
import java.time.LocalDate;

/**
 * DTO para representar el estado de una habitación en un día específico.
 * Contiene la fecha y el estado de la habitación.
 * Utilizado para transferir datos entre capas de la aplicación.
 */

@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor
@AllArgsConstructor // Genera el constructor con todos los argumentos
@Schema(description = "Representa el estado de una habitación en una fecha específica.")
public class EstadoDiaDTO {

    @Schema(description = "Fecha específica del estado (Formato ISO: YYYY-MM-DD)", example = "2025-05-20")
    private LocalDate fecha;

    @Schema(description = "Estado de la habitación en esa fecha", example = "DISPONIBLE")
    private EstadoHabitacion estado;


}