package org.TPDesarrollo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
/**
 * DTO para crear una nueva reserva o registrar una ocupación (Check-in).
 * Contiene validaciones para asegurar la integridad de los datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear una nueva reserva o registrar una ocupación (Check-in).")
public class ReservaDTO {

    @Schema(description = "ID de la reserva existente (solo para modificaciones o referencias).", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Fecha de ingreso (Check-in). Debe ser hoy o futura (YYYY-MM-DD).", example = "2025-12-08")
    @NotNull(message = "La fecha de ingreso es obligatoria")
    @FutureOrPresent(message = "La fecha de ingreso debe ser hoy o posterior")
    private LocalDate ingreso;

    @Schema(description = "Fecha de egreso (Check-out). Debe ser estrictamente futura (YYYY-MM-DD).", example = "2025-12-15")
    @NotNull(message = "La fecha de egreso es obligatoria")
    @Future(message = "La fecha de egreso debe ser futura")
    private LocalDate egreso;

    @Schema(description = "Datos del titular o responsable de la reserva.")
    @NotNull(message = "Los datos del titular son obligatorios")
    @Valid
    private DatosHuespedReserva huesped;

    @Schema(description = "Lista de IDs de las habitaciones seleccionadas para esta reserva/ocupación.", example = "[105, 106]")
    @NotEmpty(message = "Debe seleccionar al menos una habitación")
    private List<Integer> habitaciones;

    @Schema(description = "Mapa de ocupantes por cada ID de habitación. Clave: ID Habitación; Valor: Lista de OcupantesDTOs.")
    private Map<Integer, List<@Valid OcupanteDTO>> ocupantesPorHabitacion;


    // --- Inner Class: Datos básicos del Huésped ---

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Datos mínimos del huésped requeridos para la reserva/ocupación.")
    public static class DatosHuespedReserva {

        @Schema(description = "Tipo de documento (ej: DNI).", example = "DNI")
        @NotBlank(message = "Tipo Doc obligatorio")
        private String tipoDocumento;

        @Schema(description = "Número de documento.", example = "30123456")
        @NotBlank(message = "Documento obligatorio")
        @Pattern(regexp = "^[0-9]*$", message = "El documento solo puede contener números")
        private String documento;

        @Schema(description = "Nombre del titular.", example = "María")
        @NotBlank(message = "Nombre obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El nombre solo puede contener letras y espacios")
        private String nombre;

        @Schema(description = "Apellido del titular.", example = "García")
        @NotBlank(message = "Apellido obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El apellido solo puede contener letras y espacios")
        private String apellido;

        @Schema(description = "Teléfono de contacto.", example = "3411234567")
        @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo puede contener números")
        private String telefono;
    }
}