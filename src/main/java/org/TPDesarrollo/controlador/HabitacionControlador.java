package org.TPDesarrollo.controlador;

import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.gestores.GestorHabitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
// Ajusta el puerto según donde corra tu React/Frontend
@CrossOrigin(origins = "http://localhost:3000")
public class HabitacionControlador {

    private final GestorHabitacion gestorHabitacion;

    @Autowired
    public HabitacionControlador(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
    }

    // Endpoint: GET /api/habitaciones/estado?desde=2023-10-01&hasta=2023-10-15
    @GetMapping("/estado")
    public ResponseEntity<?> obtenerEstado(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        try {
            // Llama al gestor siguiendo el diagrama de secuencia [cite: 22]
            List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(fechaDesde, fechaHasta);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            // Manejo de errores de validación de fechas
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener estados");
        }
    }
}