package org.TPDesarrollo.mappers;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dto.HuespedDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Mapper para convertir entre la entidad Huesped y su DTO correspondiente.
 */
@Component
public class HuespedMapper {

    private final ModelMapper modelMapper;
    /**
     * Constructor que inyecta el ModelMapper.
     *
     * @param modelMapper El ModelMapper a utilizar para las conversiones.
     */
    @Autowired
    public HuespedMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    /**
     * Convierte una entidad Huesped a su DTO correspondiente.
     *
     * @param entidad La entidad Huesped a convertir.
     * @return El DTO correspondiente.
     */
    public HuespedDTO toDto(Huesped entidad) {
        if (entidad == null) return null;
        return modelMapper.map(entidad, HuespedDTO.class);
    }
    /**
     * Convierte un DTO de Huesped a su entidad correspondiente.
     *
     * @param dto El DTO de Huesped a convertir.
     * @return La entidad correspondiente.
     */
    public Huesped toEntity(HuespedDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, Huesped.class);
    }
}