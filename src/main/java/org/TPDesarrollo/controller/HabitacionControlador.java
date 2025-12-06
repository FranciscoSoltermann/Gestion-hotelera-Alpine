package org.TPDesarrollo.controller;

import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.service.GestorHabitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar las habitaciones y sus estados.
 */
@RestController
@RequestMapping("/api/habitaciones")
// Ajusta el puerto según donde corra el React/Frontend
@CrossOrigin(origins = "http://localhost:3000")
public class HabitacionControlador {

    private final GestorHabitacion gestorHabitacion;

    @Autowired
    public HabitacionControlador(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
    }

    // Endpoint para obtener el estado de las habitaciones en un rango de fechas
    @GetMapping("/estado")
    public ResponseEntity<?> obtenerEstado(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        try {
            // Llama al gestor siguiendo el diagrama de secuencia
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