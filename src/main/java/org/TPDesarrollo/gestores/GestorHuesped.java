package org.TPDesarrollo.gestores;

import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.exceptions.CuitExistente;

import org.TPDesarrollo.clases.Direccion;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.dtos.DireccionDTO;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.enums.TipoDocumento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorHuesped {

    private final HuespedRepository huespedRepository;

    @Autowired
    public GestorHuesped(HuespedRepository huespedRepository) {
        this.huespedRepository = huespedRepository;
    }

    // ==========================================================
    // BUSCAR HUESPEDES
    // ==========================================================

    @Transactional(readOnly = true)
    public List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {

        String apellidoParam = (apellido != null && !apellido.trim().isEmpty()) ? apellido.trim() : null;
        String nombreParam = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
        String documentoParam = (documento != null && !documento.trim().isEmpty()) ? documento.trim() : null;

        TipoDocumento tipoDocEnum = null;
        if (tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            try {
                tipoDocEnum = TipoDocumento.valueOf(tipoDocumento.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        List<Huesped> resultados =
                huespedRepository.buscarHuespedesPorCriterios(
                        apellidoParam, nombreParam, tipoDocEnum, documentoParam
                );

        return resultados.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // OBTENER UN HUESPED
    // ==========================================================

    @Transactional(readOnly = true)
    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        return huespedRepository.findById(idHuesped)
                .map(this::convertirA_DTO)
                .orElse(null);
    }

    // ==========================================================
    // DAR DE ALTA HUESPED
    // ==========================================================

    @Transactional
    public Huesped darDeAltaHuesped(HuespedDTO dto) {

        if (huespedRepository.existsByDocumento(dto.getDocumento())) {
            throw new DniExistente("El DNI/Documento ingresado ya existe.");
        }

        if (dto.getCuit() != null && !dto.getCuit().trim().isEmpty()) {
            if (huespedRepository.existsByCuit(dto.getCuit())) {
                throw new CuitExistente("El CUIT ya existe: " + dto.getCuit());
            }
        } else {
            dto.setPosicionIVA(RazonSocial.Consumidor_Final);
        }

        Huesped entidad = convertirA_Entidad(dto);

        return huespedRepository.save(entidad);
    }

    // ==========================================================
    // MODIFICAR
    // ==========================================================

    @Transactional
    public void modificarHuesped(HuespedDTO dto) {

        Huesped entidad = convertirA_Entidad(dto);

        huespedRepository.save(entidad);
    }

    // ==========================================================
    // BAJA
    // ==========================================================

    @Transactional
    public void darDeBajaHuesped(Integer id) {
        huespedRepository.deleteById(id);
    }

    // ==========================================================
    // CONVERSIONES DTO ↔ ENTIDAD
    // ==========================================================

    private HuespedDTO convertirA_DTO(Huesped entidad) {

        HuespedDTO dto = new HuespedDTO();
        dto.setId(entidad.getId());

        // --- CAMPOS DE PERSONA ---
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setDocumento(entidad.getDocumento());
        dto.setTelefono(entidad.getTelefono());
        dto.setTipoDocumento(entidad.getTipoDocumento());
        dto.setEmail(entidad.getEmail());
        dto.setNacionalidad(entidad.getNacionalidad());
        dto.setFechaNacimiento(entidad.getFechaNacimiento());

        if (entidad.getDireccion() != null) {
            dto.setDireccion(new DireccionDTO(entidad.getDireccion()));
        }

        // --- CAMPOS DE HUESPED ---
        dto.setCuit(entidad.getCuit());
        dto.setOcupacion(entidad.getOcupacion());
        dto.setPosicionIVA(entidad.getPosicionIVA());

        return dto;
    }

    private Huesped convertirA_Entidad(HuespedDTO dto) {

        Direccion direccionEntidad = null;
        if (dto.getDireccion() != null) {
            DireccionDTO d = dto.getDireccion();

            direccionEntidad = Direccion.builder()
                    .calle(d.getCalle())
                    .numero(d.getNumero())
                    .pais(d.getPais())
                    .provincia(d.getProvincia())
                    .localidad(d.getLocalidad())
                    .departamento(d.getDepartamento())
                    .piso(d.getPiso())
                    .codigoPostal(d.getCodigoPostal())
                    .build();
        }

        Huesped huesped = Huesped.builder()

                // --- PERSONA ---
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .documento(dto.getDocumento())
                .telefono(dto.getTelefono())
                .tipoDocumento(dto.getTipoDocumento())
                .email(dto.getEmail())
                .nacionalidad(dto.getNacionalidad())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(direccionEntidad)

                // --- HUESPED ---
                .cuit(dto.getCuit())
                .ocupacion(dto.getOcupacion())
                .posicionIVA(dto.getPosicionIVA())

                .build();

        // si es modificación
        if (dto.getId() != null) {
            huesped.setId(dto.getId());
        }

        return huesped;
    }
    @Transactional
    public Huesped buscarOCrearHuesped(org.TPDesarrollo.dtos.ReservaDTO reservaDTO) {

        // 1. Intentamos buscar si ya existe (Usamos tu repositorio directamente)
        List<Huesped> existentes = huespedRepository.buscarHuespedesPorCriterios(
                null, // No filtramos por apellido
                null, // No filtramos por nombre
                reservaDTO.getTipoDocumento(),
                reservaDTO.getNumeroDocumento()
        );

        if (!existentes.isEmpty()) {
            // Si existe, retornamos la entidad encontrada
            return existentes.get(0);
        }

        // 2. Si NO existe, reutilizamos tu método 'darDeAltaHuesped'
        // Para eso, primero adaptamos los datos de la Reserva a un HuespedDTO
        HuespedDTO nuevoDTO = new HuespedDTO();

        nuevoDTO.setNombre(reservaDTO.getNombre());
        nuevoDTO.setApellido(reservaDTO.getApellido());
        nuevoDTO.setTipoDocumento(reservaDTO.getTipoDocumento());
        nuevoDTO.setDocumento(reservaDTO.getNumeroDocumento());
        nuevoDTO.setTelefono(reservaDTO.getTelefono());
        nuevoDTO.setEmail(reservaDTO.getEmail());

        // Al reutilizar este método, ganamos las validaciones y lógica que ya tenías
        // (como asignar Consumidor Final automáticamente)
        return darDeAltaHuesped(nuevoDTO);
    }
}
