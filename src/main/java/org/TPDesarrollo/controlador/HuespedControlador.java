package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dto.HuespedDTO;
import org.TPDesarrollo.service.GestorHuesped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Huéspedes", description = "Endpoints para la gestión, búsqueda y modificación de clientes/huéspedes del hotel.")
public class HuespedControlador {

    private final GestorHuesped gestorHuesped;

    @Autowired
    public HuespedControlador(GestorHuesped gestorHuesped) {
        this.gestorHuesped = gestorHuesped;
    }

    @Operation(summary = "Dar de Alta un nuevo Huésped", description = "Crea un nuevo registro de huésped en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Huésped creado exitosamente. Retorna la entidad guardada."),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest") // REFERENCIA GLOBAL
    })
    @PostMapping("/alta")
    public ResponseEntity<?> crearHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(huespedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(huespedGuardado);
    }

    @Operation(summary = "Buscar huéspedes por filtros", description = "Devuelve una lista de huéspedes que coinciden con los parámetros de búsqueda (apellido, nombre, documento, etc.). Si no se proveen filtros, devuelve todos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de huéspedes devuelta (puede ser vacía).")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedes(
            @Parameter(description = "Apellido del huésped (búsqueda parcial)")
            @RequestParam(required = false) String apellido,

            @Parameter(description = "Nombre del huésped (búsqueda parcial)")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Tipo de documento (ej: DNI, PASAPORTE)")
            @RequestParam(required = false) String tipoDocumento,

            @Parameter(description = "Número exacto del documento")
            @RequestParam(required = false) String documento
    ) {
        List<HuespedDTO> huespedes = gestorHuesped.buscarHuespedes(
                apellido, nombre, tipoDocumento, documento
        );
        return ResponseEntity.ok(huespedes);
    }

    @Operation(summary = "Buscar huésped por DNI", description = "Busca un huésped específico por su número de DNI para fines de validación o precarga de formularios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped encontrado."),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound"), // REFERENCIA GLOBAL
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalError") // REFERENCIA GLOBAL
    })
    @GetMapping("/buscar-por-dni")
    public ResponseEntity<?> buscarPorDni(
            @Parameter(description = "Número de DNI exacto", example = "12345678")
            @RequestParam("dni") String dni) {
        try {
            HuespedDTO huesped = gestorHuesped.buscarPorDni(dni);

            if (huesped != null) {
                return ResponseEntity.ok(huesped);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener huésped por ID", description = "Busca un huésped por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped encontrado."),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound") // REFERENCIA GLOBAL
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID único del huésped", example = "5")
            @PathVariable Integer id) {
        HuespedDTO huesped = gestorHuesped.obtenerHuespedSeleccionado(id);

        if (huesped == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(huesped);
    }

    @Operation(summary = "Modificar datos de Huésped", description = "Actualiza los datos de un huésped existente. Requiere que el ID esté incluido en el DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped modificado correctamente."),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"), // REFERENCIA GLOBAL
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound") // REFERENCIA GLOBAL
    })
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        gestorHuesped.modificarHuesped(huespedDTO);
        return ResponseEntity.ok("Huésped modificado correctamente");
    }

    @Operation(summary = "Eliminar Huésped", description = "Elimina un huésped por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped eliminado exitosamente."),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest") // REFERENCIA GLOBAL
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarHuesped(
            @Parameter(description = "ID del huésped a eliminar", example = "10")
            @PathVariable Integer id) {
        try {
            gestorHuesped.eliminarHuesped(id);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Huésped eliminado correctamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}