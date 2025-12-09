package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.ResumenFacturacionDTO;
import org.TPDesarrollo.dto.SolicitudFacturaDTO;
import org.TPDesarrollo.dto.SolicitudNotaCreditoDTO;
import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.entity.NotaCredito;
import org.TPDesarrollo.service.GestorFactura;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de facturación.
 * Proporciona endpoints para previsualizar y crear facturas basadas en estadías activas.
 * Utiliza GestorFactura para la lógica de negocio.
 * Maneja excepciones para proporcionar respuestas adecuadas en caso de errores.
 */
@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@Tag(name = "Facturación", description = "Gestión del ciclo de cobro: Previsualización de costos y generación de comprobantes fiscales")
public class FacturaControlador {

    private final GestorFactura gestorFactura;
    /**
     * Endpoint para previsualizar el detalle de facturación de una habitación.
     * @param habitacion Número de la habitación a facturar.
     * @param horaSalida Hora estimada de salida para cálculo de medios días (Formato HH:mm).
     * @return Respuesta HTTP con el resumen de facturación o error en la operación.
     */
    @Operation(
            summary = "Previsualizar detalle de facturación",
            description = "Calcula los costos de estadía hasta la hora de salida indicada, suma los consumos pendientes y lista los posibles responsables de pago sin cerrar la estadía."
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen generado exitosamente (Items, Subtotales, Ocupantes)"),
            @ApiResponse(responseCode = "400", description = "Error de validación (Formato de hora inválido, habitación sin ocupantes activos)")
    })

    /**
     * Maneja la solicitud GET para previsualizar la factura.
     * @param habitacion Número de la habitación a facturar.
     * @param horaSalida Hora estimada de salida para cálculo de medios días (Formato HH:mm).
     * @return ResponseEntity con el resumen de facturación o error.
     */
    @GetMapping("/previsualizar")
    public ResponseEntity<?> previsualizarFactura(
            @Parameter(description = "Número de la habitación a facturar", example = "105")
            @RequestParam String habitacion,

            @Parameter(description = "Hora estimada de salida para cálculo de medios días (Formato HH:mm)", example = "10:00")
            @RequestParam String horaSalida) {
        try {
            LocalTime hora = LocalTime.parse(horaSalida);
            ResumenFacturacionDTO resumen = gestorFactura.buscarEstadiaParaFacturar(habitacion, hora);
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            e.printStackTrace(); // Considera usar @Slf4j en lugar de esto para producción
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    /**
     * Endpoint para crear y emitir una factura oficial.
     * @param solicitud Datos necesarios para generar la factura.
     * @return Respuesta HTTP con la factura creada o error en la operación.
     */
    @Operation(
            summary = "Crear y emitir factura",
            description = "Genera la factura oficial, asocia los items seleccionados, asigna el responsable de pago (Huésped o Tercero) y persiste la transacción."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura creada y guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al procesar (Falta responsable de pago, estadía ya facturada, datos inconsistentes)")
    })
    /**
     * Maneja la solicitud POST para crear una factura.
     * @param solicitud Objeto SolicitudFacturaDTO con los detalles para generar la factura
     * @return ResponseEntity con la factura creada o error
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearFactura(@RequestBody SolicitudFacturaDTO solicitud) {
        try {
            Factura factura = gestorFactura.generarFactura(solicitud);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error al facturar: " + e.getMessage()));
        }
    }
    /**
     * Endpoint para buscar facturas asociadas a un cliente por su documento (DNI/CUIT).
     * @param documento Documento del cliente para buscar facturas.
     * @return Respuesta HTTP con la lista de facturas encontradas o error en la operación.
     */
    @Operation(
            summary = "Buscar facturas por cliente (DNI/CUIT)",
            description = "Devuelve una lista de facturas pendientes/pagadas (no anuladas) asociadas a un documento."
    )
    /**
     * Maneja la solicitud GET para buscar facturas por documento de cliente.
     * @param documento Documento del cliente para buscar facturas
     * @return ResponseEntity con la lista de facturas encontradas o error
     */
    @GetMapping("/buscar-por-cliente")
    public ResponseEntity<?> buscarFacturasPorCliente(@RequestParam String documento) {
        try {
            List<Factura> resultados = gestorFactura.buscarFacturasPorCliente(documento);
            if (resultados.isEmpty()) {
                return ResponseEntity.status(404).body(Collections.singletonMap("mensaje", "No se encontraron facturas activas para el documento ingresado."));
            }
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    /**
     * Endpoint para generar una Nota de Crédito que anula una o varias facturas.
     * @param solicitud Datos necesarios para generar la Nota de Crédito.
     * @return Respuesta HTTP con la Nota de Crédito creada o error en la operación.
     */
    @Operation(
            summary = "Generar Nota de Crédito",
            description = "Anula una o varias facturas seleccionadas y genera el comprobante de nota de crédito."
    )
    /**
     * Maneja la solicitud POST para crear una Nota de Crédito.
     * @param solicitud Objeto SolicitudNotaCreditoDTO con los detalles para generar la Nota de Crédito
     * @return ResponseEntity con la Nota de Crédito creada o error
     */
    @PostMapping("/nota-credito")
    public ResponseEntity<?> crearNotaCredito(@RequestBody @Valid SolicitudNotaCreditoDTO solicitud) {
        try {
            NotaCredito nuevaNota = gestorFactura.generarNotaCredito(solicitud);
            return ResponseEntity.ok(nuevaNota);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error al generar Nota de Crédito: " + e.getMessage()));
        }
    }


}