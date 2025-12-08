package org.TPDesarrollo.controlador;

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
public class PagoControlador {

    private final GestorPagoImp gestorPago;
    private final FacturaRepository facturaRepository;

    @GetMapping("/pendientes")
    public ResponseEntity<?> buscarPendientes(@RequestParam String habitacion) {

        List<Factura> pendientes = gestorPago.obtenerPendientesConSaldo(habitacion);

        if (pendientes.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "No hay facturas pendientes..."));
        }
        return ResponseEntity.ok(pendientes);
    }

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