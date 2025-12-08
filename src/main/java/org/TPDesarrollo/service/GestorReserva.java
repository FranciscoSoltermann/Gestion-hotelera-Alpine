package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface GestorReserva {

    List<Reserva> crearReserva(ReservaDTO dto);

    List<Reserva> crearOcupacion(ReservaDTO dto);

    void eliminarReserva(Integer id) throws Exception;

    void cancelarReservaPorFecha(Integer idHabitacion, LocalDate fecha) throws Exception;
}