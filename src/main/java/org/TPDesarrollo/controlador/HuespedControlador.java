package org.TPDesarrollo.controlador;

import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.gestores.GestorHuesped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde React
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

    // --- 2. BÚSQUEDA GENERAL (GET con filtros) ---
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

    // --- NUEVO: BÚSQUEDA EXACTA POR DNI (GET) ---
    @GetMapping("/buscar-por-dni")
    public ResponseEntity<?> buscarPorDni(@RequestParam("dni") String dni) {
        try {
            HuespedDTO huesped = gestorHuesped.buscarPorDni(dni);

            if (huesped != null) {
                return ResponseEntity.ok(huesped);
            } else {
                // Devolvemos 404 Not Found si no existe, para que el front sepa que debe dejar escribir
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar: " + e.getMessage());
        }
    }

    // --- 3. OBTENER POR ID (GET /{id}) ---
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        HuespedDTO huesped = gestorHuesped.obtenerHuespedSeleccionado(id);

        if (huesped == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(huesped);
    }

    // --- 4. MODIFICAR (PUT) ---
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        gestorHuesped.modificarHuesped(huespedDTO);
        return ResponseEntity.ok("Huésped modificado correctamente");
    }

    // --- 5. DAR DE BAJA (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHuesped(@PathVariable Integer id) {
        gestorHuesped.darDeBajaHuesped(id);
        return ResponseEntity.ok("Huésped eliminado correctamente");
    }
}