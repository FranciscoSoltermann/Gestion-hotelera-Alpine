package org.TPDesarrollo.mappers;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.Reserva;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Mapper para convertir entre la entidad Reserva y su DTO correspondiente.
 */
@Component
public class ReservaMapper {

    private final ModelMapper modelMapper;
    /**
     * Constructor que inyecta el ModelMapper.
     * @param modelMapper El ModelMapper a utilizar para las conversiones.
     */
    @Autowired
    public ReservaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    /**
     * Convierte una entidad Reserva a su DTO correspondiente.
     * @param reserva La entidad Reserva a convertir.
     * @return El DTO ReservaDTO resultante de la conversión.
     */
    public ReservaDTO toDto(Reserva reserva) {
        if(reserva == null) return null;
        return modelMapper.map(reserva, ReservaDTO.class);
    }
}