package org.TPDesarrollo.mappers;

import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.dto.GrillaHabitacionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public HabitacionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GrillaHabitacionDTO toGrillaDto(Habitacion habitacion) {
        if (habitacion == null) return null;

        GrillaHabitacionDTO dto = modelMapper.map(habitacion, GrillaHabitacionDTO.class);

        dto.setIdHabitacion(habitacion.getIdHabitacion());

        dto.setTipo(habitacion.getClass().getSimpleName());

        return dto;
    }
}