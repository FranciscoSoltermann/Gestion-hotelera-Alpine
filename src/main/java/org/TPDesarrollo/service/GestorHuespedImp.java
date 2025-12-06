package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.exceptions.CuitExistente;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.mappers.HuespedMapper;
import org.TPDesarrollo.repository.HuespedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorHuespedImp implements GestorHuesped {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;

    @Autowired
    public GestorHuespedImp(HuespedRepository huespedRepository, HuespedMapper huespedMapper) {
        this.huespedRepository = huespedRepository;
        this.huespedMapper = huespedMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedDTO buscarPorDni(String dni) {
        Huesped huesped = huespedRepository.findByDocumento(dni);
        return huespedMapper.toDto(huesped);
    }

    @Override
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

        List<Huesped> resultados = huespedRepository.buscarHuespedesPorCriterios(
                apellidoParam, nombreParam, tipoDocEnum, documentoParam
        );

        return resultados.stream()
                .map(huespedMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        return huespedRepository.findById(idHuesped)
                .map(huespedMapper::toDto)
                .orElse(null);
    }

    @Override
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

        Huesped entidad = huespedMapper.toEntity(dto);
        return huespedRepository.save(entidad);
    }

    @Override
    @Transactional
    public void modificarHuesped(HuespedDTO dto) {
        Huesped entidad = huespedMapper.toEntity(dto);
        huespedRepository.save(entidad);
    }

    @Override
    @Transactional
    public void darDeBajaHuesped(Integer id) {
        huespedRepository.deleteById(id);
    }
}