package org.TPDesarrollo.mappers;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dto.HuespedDTO;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public class HuespedMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public HuespedMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public HuespedDTO toDto(Huesped entidad) {
        if (entidad == null) return null;
        return modelMapper.map(entidad, HuespedDTO.class);
    }

    public Huesped toEntity(HuespedDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, Huesped.class);
    }
}