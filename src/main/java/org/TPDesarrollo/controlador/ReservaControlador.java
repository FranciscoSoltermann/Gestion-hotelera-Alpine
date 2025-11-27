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
// @CrossOrigin ya está en tu configuración global, pero no hace daño dejarlo
public class ReservaControlador {

    private final GestorReserva gestorReserva;

    public ReservaControlador(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaDTO dto) {
        try {
            // 1. Ejecutamos la lógica (Esto guardará en la BD)
            gestorReserva.crearReserva(dto);

            // 2. ⭐ EL CAMBIO CLAVE ⭐
            // En lugar de devolver el objeto 'Reserva' complejo, devolvemos un JSON simple.
            // Esto evita el 100% de los errores de "Failed to fetch" por culpa del JSON infinito.
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Reserva creada con éxito");

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            // 3. Imprimimos el error real en la consola de IntelliJ para verlo
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/ocupar")
    public ResponseEntity<?> ocuparHabitacion(@Valid @RequestBody ReservaDTO dto) {
        try {
            // Llamamos al nuevo método del gestor
            gestorReserva.crearOcupacion(dto);

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Habitación ocupada con éxito (Check-in realizado)");

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al ocupar: " + e.getMessage());
        }
    }
}