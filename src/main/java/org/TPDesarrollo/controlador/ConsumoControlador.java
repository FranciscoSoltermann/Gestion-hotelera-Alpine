package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.CargaConsumoDTO;
import org.TPDesarrollo.service.GestorConsumoImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Controlador REST para la gestión de consumos adicionales en habitaciones.
 * Proporciona endpoints para cargar consumos como frigobar, lavandería o daños a la cuenta corriente de una estadía.
 * Utiliza GestorConsumoImp para la lógica de negocio.
 * Maneja excepciones para proporcionar respuestas adecuadas en caso de errores.
 */
@RestController
@RequestMapping("/api/consumos")
@RequiredArgsConstructor
@Tag(name = "Consumos", description = "Gestión de gastos adicionales (frigobar, lavandería, daños) en las habitaciones")
public class ConsumoControlador {

    private final GestorConsumoImp gestorConsumo;

    /**
     * Endpoint para cargar un consumo a la cuenta corriente de una habitación.
     * @param dto Datos del consumo a registrar (habitacionId, descripcion, monto).
     * @return Respuesta HTTP indicando éxito o error en la operación.
     */
    @Operation(
            summary = "Cargar un consumo a una habitación",
            description = "Registra un ítem (producto o servicio) en la cuenta corriente de la estadía activa de una habitación específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumo registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación (Habitación vacía, no existe, o datos del consumo incorrectos)")
    })
    /**
     * Maneja la solicitud POST para cargar un consumo.
     * @param dto Objeto CargaConsumoDTO con los detalles del consumo a registrar
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/cargar")
    public ResponseEntity<?> cargarConsumo(@RequestBody CargaConsumoDTO dto) {
        try {
            gestorConsumo.cargarConsumo(dto);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Consumo cargado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}