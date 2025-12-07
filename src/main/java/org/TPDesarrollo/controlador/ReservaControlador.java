package org.TPDesarrollo.controlador;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.service.GestorReserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservas")
// CORRECCIÓN CRÍTICA AQUÍ:
// Cambiamos 'origins = "*"' por 'originPatterns = "*"'
// Esto permite credenciales y evita el error "IllegalArgumentException" de Spring Security.
@CrossOrigin(originPatterns = "*")
public class ReservaControlador {

    private final GestorReserva gestorReserva;

    public ReservaControlador(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearReserva(dto);
            return responderExito("Reserva creada con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return responderError(e.getMessage());
        }
    }

    @PostMapping("/ocupar")
    public ResponseEntity<?> ocuparHabitacion(@Valid @RequestBody ReservaDTO dto) {
        try {
            gestorReserva.crearOcupacion(dto);
            return responderExito("Habitación ocupada con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return responderError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Integer id) {
        try {
            gestorReserva.eliminarReserva(id);
            return responderExito("Reserva eliminada con éxito");
        } catch (Exception e) {
            return responderError(e.getMessage());
        }
    }

    @DeleteMapping("/cancelar")
    public ResponseEntity<?> cancelarReservaPorFecha(
            @RequestParam Integer idHabitacion,
            @RequestParam String fecha
    ) {
        try {
            LocalDate fechaConsulta = LocalDate.parse(fecha);
            gestorReserva.cancelarReservaPorFecha(idHabitacion, fechaConsulta);
            return responderExito("Reserva cancelada con éxito para la fecha " + fecha);
        } catch (Exception e) {
            e.printStackTrace();
            return responderError("Error al cancelar: " + e.getMessage());
        }
    }

    private ResponseEntity<Map<String, String>> responderExito(String mensaje) {
        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", mensaje);
        return ResponseEntity.ok(resp);
    }

    private ResponseEntity<Map<String, String>> responderError(String mensaje) {
        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(resp);
    }
}