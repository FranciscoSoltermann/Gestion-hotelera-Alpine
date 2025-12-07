package org.TPDesarrollo.dto;

import lombok.Data;
import java.util.List;

@Data
public class GrillaHabitacionDTO {
    private Integer idHabitacion;
    private String numero;
    private String tipo;
    private Integer capacidad;
    private List<EstadoDiaDTO> estadosPorDia;
}