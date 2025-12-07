package org.TPDesarrollo.controller;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.ResumenFacturacionDTO;
import org.TPDesarrollo.dto.SolicitudFacturaDTO;
import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.service.GestorFactura;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Collections; // Importante
import java.util.Map;         // Importante

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaControlador {

    private final GestorFactura gestorFactura;

    @GetMapping("/previsualizar")
    public ResponseEntity<?> previsualizarFactura(
            @RequestParam String habitacion,
            @RequestParam String horaSalida) {
        try {
            LocalTime hora = LocalTime.parse(horaSalida);
            ResumenFacturacionDTO resumen = gestorFactura.buscarEstadiaParaFacturar(habitacion, hora);
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

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
}