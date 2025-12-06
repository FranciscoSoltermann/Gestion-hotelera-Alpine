package org.TPDesarrollo.controller;

import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.service.GestorReserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestionar reservas y ocupaciones de habitaciones.
 * Proporciona endpoints para crear reservas y ocupar habitaciones.
 * Maneja respuestas exitosas y errores en formato JSON.
 * Utiliza la clase GestorReserva para la lógica de negocio.
 * Los endpoints son accesibles sin autenticación.
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaControlador {

    private final GestorReserva gestorReserva;

    public ReservaControlador(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }


    @PostMapping
    // Endpoint para crear una nueva reserva
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearReserva(dto);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Reserva creada con éxito");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage()); // Pasamos el mensaje real al front
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/ocupar")
    // Endpoint para ocupar una habitación (check-in)
    public ResponseEntity<?> ocuparHabitacion(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearOcupacion(dto);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Habitación ocupada con éxito");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}