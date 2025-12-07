package org.TPDesarrollo.mappers;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.Reserva;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ReservaMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ReservaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservaDTO toDto(Reserva reserva) {
        if(reserva == null) return null;
        return modelMapper.map(reserva, ReservaDTO.class);
    }
}