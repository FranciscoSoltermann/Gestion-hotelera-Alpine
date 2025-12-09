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
/**
 * Controlador REST para la gestión de reservas y ocupaciones.
 * Proporciona endpoints para crear reservas, registrar check-ins (ocupaciones) y cancelar reservas.
 * Utiliza GestorReserva para la lógica de negocio.
 * Maneja excepciones para proporcionar respuestas adecuadas en caso de errores.
 */
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(originPatterns = "*")
@Tag(name = "Reservas y Ocupaciones", description = "Endpoints para la gestión de reservas, check-in (ocupación) y cancelación de estadías.")
public class ReservaControlador {

    private final GestorReserva gestorReserva;
    /**
     * Constructor del controlador de reservas.
     */
    public ReservaControlador(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }
    /**
     * Endpoint para crear una nueva reserva.
     * @param dto Datos de la reserva a registrar.
     * @return Respuesta HTTP con el resultado de la operación.
     */
    @Operation(summary = "Crear una nueva reserva", description = "Registra una reserva para fechas futuras. No implica check-in inmediato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, fechas en el pasado, o habitación no disponible para el rango solicitado.")
    })
    /**
     * Maneja la solicitud POST para crear una nueva reserva.
     * @param dto Objeto ReservaDTO con los detalles de la reserva a registrar
     * @return ResponseEntity con el resultado de la operación
     */
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
    /**
     * Endpoint para registrar una ocupación (check-in) inmediata.
     * @param dto Datos de la reserva/ocupación a registrar.
     * @return Respuesta HTTP con el resultado de la operación.
     */
    @Operation(summary = "Registrar ocupación (Check-in)", description = "Crea una estadía y la marca como ACTIVA. Se utiliza para el ingreso inmediato o el check-in de una reserva existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación ocupada con éxito (Check-in realizado)."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. la habitación ya está ocupada o las fechas son inconsistentes).")
    })
    /**
     * Maneja la solicitud POST para registrar una ocupación.
     * @param dto Objeto ReservaDTO con los detalles de la ocupación a registrar
     * @return ResponseEntity con el resultado de la operación
     */
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
    /**
     * Endpoint para eliminar una reserva por su ID.
     * @param id ID único de la reserva a eliminar.
     * @return Respuesta HTTP con el resultado de la operación.
     */
    @Operation(summary = "Eliminar reserva por ID", description = "Cancela una reserva completa de forma permanente utilizando el ID único de la reserva.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva eliminada con éxito."),
            @ApiResponse(responseCode = "400", description = "Error al eliminar (ej. la reserva no existe o ya está activa).")
    })
    /** Maneja la solicitud DELETE para eliminar una reserva por ID.
     * @param id ID único de la reserva a eliminar.
     * @return ResponseEntity con el resultado de la operación
     */
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
    /**
     * Endpoint para cancelar una reserva por una fecha/día específico.
     * @param idHabitacion ID de la habitación cuya reserva se desea cancelar.
     * @param fecha Fecha específica del día a cancelar (Formato ISO: YYYY-MM-DD).
     * @return Respuesta HTTP con el resultado de la operación.
     */
    @Operation(summary = "Cancelar reserva por fecha/día", description = "Cancela la reserva para un día específico dentro de un rango de estadía. Útil para la funcionalidad de 'eliminar celda' en la matriz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada con éxito para la fecha específica."),
            @ApiResponse(responseCode = "400", description = "Error al cancelar (la celda no estaba reservada o la fecha no es válida).")
    })
    /** Maneja la solicitud DELETE para cancelar una reserva por fecha específica.
     * @param idHabitacion ID de la habitación cuya reserva se desea cancelar.
     * @param fecha Fecha específica del día a cancelar (Formato ISO: YYYY-MM-DD).
     * @return ResponseEntity con el resultado de la operación
     */
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

    // Métodos utilitarios
    /**
     * Método utilitario para construir una respuesta de éxito con un mensaje.
     * @param mensaje Mensaje de éxito a incluir en la respuesta.
     * @return ResponseEntity con el mensaje de éxito.
     */
    private ResponseEntity<Map<String, String>> responderExito(String mensaje) {
        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", mensaje);
        return ResponseEntity.ok(resp);
    }
    /**
     * Método utilitario para construir una respuesta de error con un mensaje.
     * @param mensaje Mensaje de error a incluir en la respuesta.
     * @return ResponseEntity con el mensaje de error.
     */
    private ResponseEntity<Map<String, String>> responderError(String mensaje) {
        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(resp);
    }
}