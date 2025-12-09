package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * DTO que representa una habitación y su estado de ocupación a lo largo de un rango de fechas.
 * Utilizado para la matriz de disponibilidad.
 */
@Data
@Schema(description = "Representa una habitación y su estado de ocupación a lo largo de un rango de fechas. Utilizado para la matriz de disponibilidad.")
public class GrillaHabitacionDTO {

    @Schema(description = "ID único de la habitación.", example = "15")
    private Integer idHabitacion;

    @Schema(description = "Número o identificador visible de la habitación.", example = "105")
    private String numero;

    @Schema(description = "Tipo de habitación (ej: Doble Superior, Individual Estándar).", example = "IndividualEstandar")
    private String tipo;

    @Schema(description = "Capacidad máxima de ocupantes.", example = "2")
    private Integer capacidad;

    @Schema(description = "Lista de estados que tiene la habitación para cada día del rango solicitado.")
    private List<EstadoDiaDTO> estadosPorDia;
}