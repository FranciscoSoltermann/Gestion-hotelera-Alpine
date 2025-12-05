package org.TPDesarrollo.service;

import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.ReservaDTO;
import java.util.List;

public interface GestorReserva {

    List<Reserva> crearReserva(ReservaDTO dto);

    List<Reserva> crearOcupacion(ReservaDTO dto);
}