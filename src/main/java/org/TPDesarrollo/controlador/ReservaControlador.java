package org.TPDesarrollo.controlador;

import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.gestores.GestorReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaControlador {

    @Autowired
    private GestorReserva gestorReserva;

    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            gestorReserva.registrarReserva(reservaDTO);
            return ResponseEntity.ok("Reserva registrada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear reserva: " + e.getMessage());
        }
    }
}