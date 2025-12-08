package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.PagoDTO;
import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.repository.FacturaRepository;
import org.TPDesarrollo.service.GestorPagoImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.TPDesarrollo.enums.EstadoFactura;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Endpoints para la gestión, búsqueda y registro de pagos asociados a facturas.")
public class PagoControlador {

    private final GestorPagoImp gestorPago;
    private final FacturaRepository facturaRepository; // (Aunque no se usa en los métodos, se deja por si la lógica lo requiere en el futuro)

    @Operation(
            summary = "Buscar facturas pendientes de pago",
            description = "Obtiene todas las facturas abiertas o con saldo pendiente asociadas a la estadía activa de una habitación específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de facturas pendientes devuelta."),
            @ApiResponse(responseCode = "404", description = "No se encontraron facturas pendientes para esa habitación."),
            @ApiResponse(responseCode = "400", description = "Error en el formato de la habitación solicitada.")
    })
    @GetMapping("/pendientes")
    public ResponseEntity<?> buscarPendientes(
            @Parameter(description = "Número de habitación para buscar facturas", example = "105")
            @RequestParam String habitacion) {

        List<Factura> pendientes = gestorPago.obtenerPendientesConSaldo(habitacion);

        if (pendientes.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "No hay facturas pendientes..."));
        }
        return ResponseEntity.ok(pendientes);
    }

    @Operation(
            summary = "Registrar un pago",
            description = "Aplica un monto de pago a una factura específica, actualizando su saldo y estado (de 'PENDIENTE' a 'PAGADA' si el saldo es cero)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago registrado y factura actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de pago inválidos, monto insuficiente o excesivo, o factura no existente.")
    })
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPago(@RequestBody PagoDTO pagoDTO) {
        try {
            Factura facturaActualizada = gestorPago.registrarPago(pagoDTO);
            return ResponseEntity.ok(facturaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}