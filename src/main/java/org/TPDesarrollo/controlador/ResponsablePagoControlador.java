package org.TPDesarrollo.controlador;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.ResponsableDePagoDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.repository.ResponsableDePagoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/responsables")
@RequiredArgsConstructor
public class ResponsablePagoControlador {

    private final ResponsableDePagoRepository responsableRepository;

    @PostMapping("/crear-juridica")
    public ResponseEntity<?> crearResponsableJuridico(@RequestBody ResponsableDePagoDTO dto) {
        try {
            // --- VALIDACIÓN DE CUIT DUPLICADO ---
            // Verificamos si ya existe alguien con ese CUIT
            Optional<PersonaJuridica> existente = responsableRepository.findByCuit(dto.getCuit());

            if (existente.isPresent()) {
                // Si existe, devolvemos un error 400 con mensaje JSON claro
                Map<String, String> error = Collections.singletonMap("message", "El CUIT " + dto.getCuit() + " ya se encuentra registrado en el sistema.");
                return ResponseEntity.badRequest().body(error);
            }
            // ------------------------------------

            // 1. Crear la Dirección
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

    @GetMapping("/buscar-cuit")
    public ResponseEntity<?> buscarPorCuit(@RequestParam String cuit) {
        return responsableRepository.findByCuit(cuit)
                .map(pj -> {
                    // Si existe, devolvemos el DTO con los datos
                    ResponsableDePagoDTO dto = new ResponsableDePagoDTO();
                    dto.setId(pj.getId());
                    dto.setRazonSocial(pj.getRazonSocial());
                    dto.setCuit(pj.getCuit());
                    dto.setTelefono(pj.getTelefono());
                    // Puedes agregar dirección si quieres precargarla también
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }
}