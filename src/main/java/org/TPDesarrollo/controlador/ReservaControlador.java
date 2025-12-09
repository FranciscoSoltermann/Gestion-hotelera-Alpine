package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.service.GestorReserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservas")
// CORRECCIÓN CRÍTICA AQUÍ:
// Usar originPatterns es correcto para CORS con credenciales
@CrossOrigin(originPatterns = "*")
@Tag(name = "Reservas y Ocupaciones", description = "Endpoints para la gestión de reservas, check-in (ocupación) y cancelación de estadías.")
public class ReservaControlador {

    private final GestorReserva gestorReserva;

    public ReservaControlador(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }

    @Operation(summary = "Crear una nueva reserva", description = "Registra una reserva para fechas futuras. No implica check-in inmediato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, fechas en el pasado, o habitación no disponible para el rango solicitado.")
    })
    @PostMapping
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearReserva(dto);
            return responderExito("Reserva creada con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            // Esto será capturado por el Global Exception Handler si está configurado
            return responderError(e.getMessage());
        }
    }

    @Operation(summary = "Registrar ocupación (Check-in)", description = "Crea una estadía y la marca como ACTIVA. Se utiliza para el ingreso inmediato o el check-in de una reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación ocupada con éxito (Check-in realizado)."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. la habitación ya está ocupada o las fechas son inconsistentes).")
    })
    @PostMapping("/ocupar")
    public ResponseEntity<?> ocuparHabitacion(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearOcupacion(dto);
            return responderExito("Habitación ocupada con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return responderError(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar reserva por ID", description = "Cancela una reserva completa de forma permanente utilizando el ID único de la reserva.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva eliminada con éxito."),
            @ApiResponse(responseCode = "400", description = "Error al eliminar (ej. la reserva no existe o ya está activa).")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(
            @Parameter(description = "ID de la reserva a eliminar", example = "5")
            @PathVariable Integer id) {
        try {
            gestorReserva.eliminarReserva(id);
            return responderExito("Reserva eliminada con éxito");
        } catch (Exception e) {
            return responderError(e.getMessage());
        }
    }

    @Operation(summary = "Cancelar reserva por fecha/día", description = "Cancela la reserva para un día específico dentro de un rango de estadía. Útil para la funcionalidad de 'eliminar celda' en la matriz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada con éxito para la fecha específica."),
            @ApiResponse(responseCode = "400", description = "Error al cancelar (la celda no estaba reservada o la fecha no es válida).")
    })
    @DeleteMapping("/cancelar")
    public ResponseEntity<?> cancelarReservaPorFecha(
            @Parameter(description = "ID de la habitación", example = "105")
            @RequestParam Integer idHabitacion,

            @Parameter(description = "Fecha específica del día a cancelar (Formato ISO: YYYY-MM-DD)", example = "2025-02-10")
            @RequestParam String fecha
    ) {
        try {
            LocalDate fechaConsulta = LocalDate.parse(fecha);
            gestorReserva.cancelarReservaPorFecha(idHabitacion, fechaConsulta);
            return responderExito("Reserva cancelada con éxito para la fecha " + fecha);
        } catch (Exception e) {
            e.printStackTrace();
            return responderError("Error al cancelar: " + e.getMessage());
        }
    }

    // Métodos utilitarios (no necesitan anotación Swagger, ya que no son endpoints)
    private ResponseEntity<Map<String, String>> responderExito(String mensaje) {
        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", mensaje);
        return ResponseEntity.ok(resp);
    }

    private ResponseEntity<Map<String, String>> responderError(String mensaje) {
        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(resp);
    }
}