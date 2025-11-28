package org.TPDesarrollo.controlador;

import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.gestores.GestorReserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaControlador {

    private final GestorReserva gestorReserva;

    public ReservaControlador(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }

    // Endpoint para botón VERDE (Reservar)
    @PostMapping
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearReserva(dto);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Reserva creada con éxito");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // Endpoint para botón AZUL (Ocupar/Check-in)
    @PostMapping("/ocupar")
    public ResponseEntity<?> ocuparHabitacion(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearOcupacion(dto);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Habitación ocupada con éxito");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}