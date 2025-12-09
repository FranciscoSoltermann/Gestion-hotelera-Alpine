package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dto.HuespedDTO;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.exceptions.CuitExistente;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.mappers.HuespedMapper;
import org.TPDesarrollo.repository.HuespedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Implementación del gestor de huéspedes.
 */
@Service
public class GestorHuespedImp implements GestorHuesped {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;
    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param huespedRepository Repositorio de huéspedes.
     * @param huespedMapper     Mapper para convertir entre entidades y DTOs.
     */
    @Autowired
    public GestorHuespedImp(HuespedRepository huespedRepository, HuespedMapper huespedMapper) {
        this.huespedRepository = huespedRepository;
        this.huespedMapper = huespedMapper;
    }
    /**
     * Busca un huésped por su DNI.
     *
     * @param dni Documento Nacional de Identidad del huésped.
     * @return DTO del huésped encontrado o null si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public HuespedDTO buscarPorDni(String dni) {
        Huesped huesped = huespedRepository.findByDocumento(dni);
        // El mapper se encarga de verificar si es null y convertirlo
        return huespedMapper.toDto(huesped);
    }
    /**
     * Busca huéspedes según los criterios proporcionados.
     *
     * @param apellido      Apellido del huésped.
     * @param nombre        Nombre del huésped.
     * @param tipoDocumento Tipo de documento del huésped.
     * @param documento     Documento del huésped.
     * @return Lista de DTOs de huéspedes que coinciden con los criterios.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {

        // 1. Limpieza de parámetros (Igual que tu original)
        String apellidoParam = (apellido != null && !apellido.trim().isEmpty()) ? apellido.trim() : null;
        String nombreParam = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
        String documentoParam = (documento != null && !documento.trim().isEmpty()) ? documento.trim() : null;

        // 2. Conversión segura de Enum
        TipoDocumento tipoDocEnum = null;
        if (tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            try {
                tipoDocEnum = TipoDocumento.valueOf(tipoDocumento.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // Si el tipo de documento no es válido, se ignora en el filtro
            }
        }

        // 3. Búsqueda en repositorio
        List<Huesped> resultados = huespedRepository.buscarHuespedesPorCriterios(
                apellidoParam, nombreParam, tipoDocEnum, documentoParam
        );

        // 4. Conversión a DTO usando Stream y Mapper
        return resultados.stream()
                .map(huespedMapper::toDto)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene un huésped seleccionado por su ID.
     *
     * @param idHuesped ID del huésped.
     * @return DTO del huésped encontrado o null si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        return huespedRepository.findById(idHuesped)
                .map(huespedMapper::toDto)
                .orElse(null);
    }
    /**
     * Da de alta un nuevo huésped.
     *
     * @param dto DTO del huésped a dar de alta.
     * @return Entidad del huésped dado de alta.
     */
    @Override
    @Transactional
    public Huesped darDeAltaHuesped(HuespedDTO dto) {
        // 1. Validaciones de negocio (Igual que tu original)
        if (huespedRepository.existsByDocumento(dto.getDocumento())) {
            throw new DniExistente("El DNI/Documento ingresado ya existe.");
        }

        if (dto.getCuit() != null && !dto.getCuit().trim().isEmpty()) {
            if (huespedRepository.existsByCuit(dto.getCuit())) {
                throw new CuitExistente("El CUIT ya existe: " + dto.getCuit());
            }
        } else {
            // Regla de negocio: Si no hay CUIT, es Consumidor Final
            dto.setPosicionIVA(RazonSocial.Consumidor_Final);
        }

        // 2. Conversión y Guardado
        Huesped entidad = huespedMapper.toEntity(dto);
        return huespedRepository.save(entidad);
    }
    /**
     * Modifica un huésped existente.
     *
     * @param dto DTO del huésped a modificar.
     */
    @Override
    @Transactional
    public void modificarHuesped(HuespedDTO dto) {
        Huesped entidad = huespedMapper.toEntity(dto);
        huespedRepository.save(entidad);
    }
    /**
     * Elimina un huésped por su ID.
     *
     * @param id ID del huésped a eliminar.
     */
    @Override
    public void eliminarHuesped(Integer id) {
        if (!huespedRepository.existsById(id)) {
            throw new RuntimeException("El huésped con ID " + id + " no existe.");
        }

        try {
            huespedRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // Esta excepción salta si el huésped tiene Reservas o Estadías asociadas (FK constraint)
            throw new RuntimeException("No se puede eliminar el huésped porque tiene Reservas o Estadías asociadas. Debe anularlas primero.");
        }
    }
}