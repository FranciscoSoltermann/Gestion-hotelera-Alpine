package org.TPDesarrollo.Controlador;

import org.TPDesarrollo.Clases.Huesped;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
public class HuespedControlador {

    @Autowired
    private GestorHuesped gestorHuesped;

    @PostMapping("/alta")
    public ResponseEntity<?> crearHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        try {
            //
            Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(huespedDTO);

            //  Devuelve el objeto Huesped completo. Spring lo convertirá en JSON.
            return ResponseEntity.ok(huespedGuardado);

        } catch (Exception e) {

            return ResponseEntity
                    .status(500)
                    .body("{\"error\":\"Error al registrar huesped: " + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarHuespedes(
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento
    ) {
        try {
            //  Llama al gestor que ya preparamos
            List<HuespedDTO> huespedes = gestorHuesped.buscarHuespedes(
                    apellido,
                    nombre,
                    tipoDocumento,
                    documento
            );

            //  Devuelve la lista de resultados
            return ResponseEntity.ok(huespedes);

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("{\"error\":\"Error al buscar huéspedes: " + e.getMessage() + "\"}");
        }
    }
}
