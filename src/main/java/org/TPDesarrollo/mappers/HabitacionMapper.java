package org.TPDesarrollo.mappers;

import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.dto.GrillaHabitacionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Mapper para convertir entidades Habitacion a sus correspondientes DTOs.
 */
@Component
public class HabitacionMapper {

    private final ModelMapper modelMapper;
    /**
     * Constructor que inyecta el ModelMapper.
     * @param modelMapper El ModelMapper a utilizar para las conversiones.
     */
    @Autowired
    public HabitacionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    /**
     * Convierte una entidad Habitacion a un GrillaHabitacionDTO.
     * @param habitacion La entidad Habitacion a convertir.
     * @return El DTO correspondiente.
     */
    public GrillaHabitacionDTO toGrillaDto(Habitacion habitacion) {
        if (habitacion == null) return null;

        GrillaHabitacionDTO dto = modelMapper.map(habitacion, GrillaHabitacionDTO.class);

        dto.setIdHabitacion(habitacion.getIdHabitacion());

        dto.setTipo(habitacion.getClass().getSimpleName());

        return dto;
    }
}