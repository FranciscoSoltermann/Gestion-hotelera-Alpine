package org.TPDesarrollo.Controlador;

import org.TPDesarrollo.Clases.Huesped;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/huespedes")
public class HuespedControlador {

    private final GestorHuesped gestorHuesped;

    @Autowired
    public HuespedControlador(GestorHuesped gestorHuesped) {
        this.gestorHuesped = gestorHuesped;
    }

    // --- 1. ALTA (POST) ---
    @PostMapping("/alta")
    public ResponseEntity<?> crearHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        // Sin try-catch, dejamos que el GlobalExceptionHandler maneje los errores
        Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(huespedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(huespedGuardado);
    }

    // --- 2. BÚSQUEDA (GET con filtros) ---
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedes(
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento
    ) {
        List<HuespedDTO> huespedes = gestorHuesped.buscarHuespedes(
                apellido, nombre, tipoDocumento, documento
        );
        return ResponseEntity.ok(huespedes);
    }

    // --- 3. OBTENER POR ID (GET /{id}) --- (¡NUEVO!)
    // Este usa 'obtenerHuespedSeleccionado' del Gestor
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        HuespedDTO huesped = gestorHuesped.obtenerHuespedSeleccionado(id);

        if (huesped == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(huesped);
    }

    // --- 4. MODIFICAR (PUT) --- (¡NUEVO!)
    // Este usa 'modificarHuesped' del Gestor
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        // Se asume que el DTO viene con el ID del huésped a modificar
        gestorHuesped.modificarHuesped(huespedDTO);
        return ResponseEntity.ok("Huésped modificado correctamente");
    }

    // --- 5. DAR DE BAJA (DELETE) --- (¡NUEVO!)
    // Este usa 'darDeBajaHuesped' del Gestor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHuesped(@PathVariable Integer id) {
        gestorHuesped.darDeBajaHuesped(id);
        return ResponseEntity.ok("Huésped eliminado correctamente");
    }
}