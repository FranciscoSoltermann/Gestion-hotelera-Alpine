package org.TPDesarrollo.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.ResponsableDePagoDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.repository.ResponsableDePagoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
/**
 * Controlador REST para la gestión de responsables de pago (entidades jurídicas).
 * Proporciona endpoints para crear y buscar responsables de pago.
 * Utiliza ResponsableDePagoRepository para la lógica de acceso a datos.
 * Maneja excepciones para proporcionar respuestas adecuadas en caso de errores.
 */
@RestController
@RequestMapping("/api/responsables")
@RequiredArgsConstructor
@Tag(name = "Responsables de Pago (Empresas)", description = "Gestión de terceros y entidades jurídicas para la facturación A.")
public class ResponsablePagoControlador {

    private final ResponsableDePagoRepository responsableRepository;
    /**
     * Endpoint para crear un nuevo responsable jurídico (empresa).
     * @param dto Datos del responsable de pago a registrar.
     * @return Respuesta HTTP con el responsable creado o error en la operación.
     */
    @Operation(
            summary = "Crear nuevo Responsable Jurídico (Empresa)",
            description = "Da de alta una Persona Jurídica (Empresa) como responsable de pago. Requiere validación previa para evitar CUIT duplicados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsable creado exitosamente. Retorna la entidad."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o el CUIT ya se encuentra registrado."),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la dirección o guardar la entidad.")
    })
    /**
     * Maneja la solicitud POST para crear un nuevo responsable jurídico.
     * @param dto Objeto ResponsableDePagoDTO con los detalles del responsable a registrar
     * @return ResponseEntity con el responsable creado o error.
     */
    @PostMapping("/crear-juridica")
    public ResponseEntity<?> crearResponsableJuridico(@RequestBody ResponsableDePagoDTO dto) {
        try {
            // --- VALIDACIÓN DE CUIT DUPLICADO ---
            Optional<PersonaJuridica> existente = responsableRepository.findByCuit(dto.getCuit());

            if (existente.isPresent()) {
                Map<String, String> error = Collections.singletonMap("message", "El CUIT " + dto.getCuit() + " ya se encuentra registrado en el sistema.");
                return ResponseEntity.badRequest().body(error);
            }
            // ------------------------------------

            // 1. Crear la Dirección (Se asume que el DTO viene bien validado por Jakarta Validation)
            Direccion direccion = Direccion.builder()
                    .calle(dto.getCalle())
                    .numero(dto.getNumero())
                    .departamento(dto.getDepartamento())
                    .piso(dto.getPiso())
                    .codigoPostal(dto.getCodigoPostal())
                    .localidad(dto.getLocalidad())
                    .provincia(dto.getProvincia())
                    .pais(dto.getPais())
                    .build();

            // 2. Crear la Empresa
            PersonaJuridica nuevaEmpresa = new PersonaJuridica();
            nuevaEmpresa.setRazonSocial(dto.getRazonSocial());
            nuevaEmpresa.setCuit(dto.getCuit());
            nuevaEmpresa.setTelefono(dto.getTelefono());
            nuevaEmpresa.setDireccion(direccion);

            // 3. Guardar
            ResponsableDePago guardado = responsableRepository.save(nuevaEmpresa);

            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = Collections.singletonMap("message", "Error al crear responsable: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    /**
     * Endpoint para buscar un responsable jurídico por su CUIT.
     * @param cuit Número de CUIT a buscar.
     * @return Respuesta HTTP con los datos del responsable encontrado o 404 si no existe.
     */
    @Operation(
            summary = "Buscar responsable por CUIT",
            description = "Busca una entidad jurídica existente por su número de CUIT. Utilizado en la interfaz de facturación a terceros."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsable encontrado. Retorna los datos principales."),
            @ApiResponse(responseCode = "404", description = "CUIT no registrado en la base de datos.")
    })
    /** Maneja la solicitud GET para buscar un responsable por CUIT.
     * @param cuit Número de CUIT a buscar
     * @return ResponseEntity con los datos del responsable encontrado o 404 si no existe.
     */
    @GetMapping("/buscar-cuit")
    public ResponseEntity<?> buscarPorCuit(
            @Parameter(description = "Número de CUIT a buscar", example = "30123456789")
            @RequestParam String cuit) {

        return responsableRepository.findByCuit(cuit)
                .map(pj -> {
                    // Si existe, devolvemos el DTO con los datos para precarga
                    ResponsableDePagoDTO dto = new ResponsableDePagoDTO();
                    dto.setId(pj.getId());
                    dto.setRazonSocial(pj.getRazonSocial());
                    dto.setCuit(pj.getCuit());
                    dto.setTelefono(pj.getTelefono());
                    // Nota: Se podría usar un Mapper (como MapStruct) aquí para un código más limpio
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }
}