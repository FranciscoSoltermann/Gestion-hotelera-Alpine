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
/**
 * Controlador REST para la gestión de huéspedes.
 * Proporciona endpoints para crear, buscar, modificar y eliminar huéspedes.
 * Utiliza GestorHuesped para la lógica de negocio.
 * Maneja excepciones para proporcionar respuestas adecuadas en caso de errores.
 */
@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Huéspedes", description = "Endpoints para la gestión, búsqueda y modificación de clientes/huéspedes del hotel.")
public class HuespedControlador {

    private final GestorHuesped gestorHuesped;
    /**
     * Constructor del controlador de huéspedes.
     */
    @Autowired
    public HuespedControlador(GestorHuesped gestorHuesped) {
        this.gestorHuesped = gestorHuesped;
    }
    /**
     * Endpoint para dar de alta un nuevo huésped.
     * @param huespedDTO Datos del huésped a registrar.
     * @return Respuesta HTTP con el huésped creado o error en la operación.
     */
    @Operation(summary = "Dar de Alta un nuevo Huésped", description = "Crea un nuevo registro de huésped en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Huésped creado exitosamente. Retorna la entidad guardada."),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest") // REFERENCIA GLOBAL
    })
    /**
     * Maneja la solicitud POST para crear un nuevo huésped.
     * @param huespedDTO Objeto HuespedDTO con los detalles del huésped a registrar
     * @return ResponseEntity con el huésped creado o error.
     */
    @PostMapping("/alta")
    public ResponseEntity<?> crearHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(huespedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(huespedGuardado);
    }
    /**
     * Endpoint para buscar huéspedes por varios filtros.
     * @param apellido Apellido del huésped (búsqueda parcial).
     * @param nombre Nombre del huésped (búsqueda parcial).
     * @param tipoDocumento Tipo de documento (ej: DNI, PASAPORTE).
     * @param documento Número exacto del documento.
     * @return Respuesta HTTP con la lista de huéspedes encontrados o vacía.
     */
    @Operation(summary = "Buscar huéspedes por filtros", description = "Devuelve una lista de huéspedes que coinciden con los parámetros de búsqueda (apellido, nombre, documento, etc.). Si no se proveen filtros, devuelve todos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de huéspedes devuelta (puede ser vacía).")
    })
    /**
     * Maneja la solicitud GET para buscar huéspedes.
     * @param apellido Apellido del huésped (búsqueda parcial).
     * @param nombre Nombre del huésped (búsqueda parcial).
     * @param tipoDocumento Tipo de documento (ej: DNI, PASAPORTE,LE,LC,OTRO).
     * @param documento Número exacto del documento.
     * @return ResponseEntity con la lista de huéspedes encontrados o vacía.
     */
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
    /**
     * Endpoint para buscar un huésped por su número de DNI.
     * @param dni Número exacto de DNI del huésped.
     * @return Respuesta HTTP con el huésped encontrado o 404 si no existe.
     */
    @Operation(summary = "Buscar huésped por DNI", description = "Busca un huésped específico por su número de DNI para fines de validación o precarga de formularios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped encontrado."),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound"), // REFERENCIA GLOBAL
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalError") // REFERENCIA GLOBAL
    })
    /**
     * Maneja la solicitud GET para buscar un huésped por DNI.
     * @param dni Número exacto de DNI del huésped.
     * @return ResponseEntity con el huésped encontrado o 404 si no existe.
     */
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
    /**
     * Endpoint para obtener un huésped por su ID.
     * @param id ID único del huésped.
     * @return Respuesta HTTP con el huésped encontrado o 404 si no existe.
     */
    @Operation(summary = "Obtener huésped por ID", description = "Busca un huésped por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped encontrado."),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound") // REFERENCIA GLOBAL
    })
    /**
     * Maneja la solicitud GET para obtener un huésped por ID.
     * @param id ID único del huésped.
     * @return ResponseEntity con el huésped encontrado o 404 si no existe.
     */
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
    /**
     * Endpoint para modificar los datos de un huésped existente.
     * @param huespedDTO Datos actualizados del huésped (debe incluir el ID).
     * @return Respuesta HTTP indicando éxito o error en la operación.
     */
    @Operation(summary = "Modificar datos de Huésped", description = "Actualiza los datos de un huésped existente. Requiere que el ID esté incluido en el DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped modificado correctamente."),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"), // REFERENCIA GLOBAL
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound") // REFERENCIA GLOBAL
    })
    /** Maneja la solicitud PUT para modificar un huésped.
     * @param huespedDTO Objeto HuespedDTO con los datos actualizados del huésped
     * @return ResponseEntity indicando éxito o error en la operación
     */
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarHuesped(@Valid @RequestBody HuespedDTO huespedDTO) {
        gestorHuesped.modificarHuesped(huespedDTO);
        return ResponseEntity.ok("Huésped modificado correctamente");
    }
    /**
     * Endpoint para eliminar un huésped por su ID.
     * @param id ID único del huésped a eliminar.
     * @return Respuesta HTTP indicando éxito o error en la operación.
     */
    @Operation(summary = "Eliminar Huésped", description = "Elimina un huésped por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Huésped eliminado exitosamente."),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest") // REFERENCIA GLOBAL
    })
    /** Maneja la solicitud DELETE para eliminar un huésped.
     * @param id ID único del huésped a eliminar
     * @return ResponseEntity indicando éxito o error en la operación
     */
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