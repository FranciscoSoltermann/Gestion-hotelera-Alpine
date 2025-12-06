package org.TPDesarrollo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaDTO {

    private Integer id;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @FutureOrPresent(message = "La fecha de ingreso debe ser hoy o posterior")
    private LocalDate ingreso;

    @NotNull(message = "La fecha de egreso es obligatoria")
    @Future(message = "La fecha de egreso debe ser futura")
    private LocalDate egreso;

    @NotNull(message = "Los datos del titular son obligatorios")
    @Valid
    private DatosHuespedReserva huesped;

    @NotEmpty(message = "Debe seleccionar al menos una habitación")
    private List<Integer> habitaciones;

    private Map<Integer, List<@Valid OcupanteDTO>> ocupantesPorHabitacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DatosHuespedReserva {

        @NotBlank(message = "Tipo Doc obligatorio")
        private String tipoDocumento;

        @NotBlank(message = "Documento obligatorio")
        @Pattern(regexp = "^[0-9]*$", message = "El documento solo puede contener números")
        private String documento;

        @NotBlank(message = "Nombre obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El nombre solo puede contener letras y espacios")
        private String nombre;

        @NotBlank(message = "Apellido obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "El apellido solo puede contener letras y espacios")
        private String apellido;

        @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo puede contener números")
        private String telefono;
    }
}