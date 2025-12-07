package org.TPDesarrollo.controlador;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.CargaConsumoDTO;
import org.TPDesarrollo.service.GestorConsumoImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/consumos")
@RequiredArgsConstructor
public class ConsumoControlador {

    private final GestorConsumoImp gestorConsumo;

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