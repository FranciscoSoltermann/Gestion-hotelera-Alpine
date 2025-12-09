package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.TPDesarrollo.dto.GrillaHabitacionDTO;
import org.TPDesarrollo.service.GestorHabitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar las habitaciones y sus estados.
 * Proporciona endpoints para consultar la disponibilidad y el estado de las habitaciones
 * en un rango de fechas específico.
 * Utiliza GestorHabitacion para la lógica de negocio.
 * Maneja excepciones para proporcionar respuestas adecuadas en caso de errores.
 */
@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Habitaciones & Disponibilidad", description = "Consulta y gestión del estado de ocupación y reservas de las habitaciones.")
public class HabitacionControlador {

    private final GestorHabitacion gestorHabitacion;

    /**
     * Constructor del controlador de habitaciones.
     */
    @Autowired
    public HabitacionControlador(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
    }
    /**
     * Endpoint para obtener el estado de todas las habitaciones en un rango de fechas.
     * @param fechaDesde Fecha de inicio del rango (Formato ISO: YYYY-MM-DD).
     * @param fechaHasta Fecha de fin del rango (Formato ISO: YYYY-MM-DD).
     * @return Respuesta HTTP con la lista de estados de habitaciones o error en la operación.
     */
    @Operation(
            summary = "Obtener Matriz de Disponibilidad",
            description = "Devuelve el estado (Disponible, Ocupada, Reservada, Mantenimiento) de todas las habitaciones para un rango de fechas. Utilizado para la grilla de recepción."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de la disponibilidad por día generado correctamente."),
            @ApiResponse(responseCode = "400", description = "Error en la validación de fechas (ej: rango inválido)."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al consultar la base de datos.")
    })
    /**
     * Maneja la solicitud GET para obtener el estado de las habitaciones.
     * @param fechaDesde Fecha de inicio del rango (Formato ISO: YYYY-MM-DD).
     * @param fechaHasta Fecha de fin del rango (Formato ISO: YYYY-MM-DD).
     * @return ResponseEntity con la lista de estados de habitaciones o error.
     */
    @GetMapping("/estado")
    public ResponseEntity<?> obtenerEstado(
            @Parameter(description = "Fecha de inicio del rango (Formato ISO: YYYY-MM-DD)", example = "2025-01-01")
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,

            @Parameter(description = "Fecha de fin del rango (Formato ISO: YYYY-MM-DD)", example = "2025-01-15")
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        try {
            List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(fechaDesde, fechaHasta);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            // Manejo de errores de validación de fechas (devuelve 400 Bad Request)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Manejo de errores generales (devuelve 500 Internal Server Error)
            return ResponseEntity.internalServerError().body("Error al obtener estados");
        }
    }
}