package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface GestorReserva {

    /**
     * Crea reservas en estado "RESERVADA".
     */
    List<Reserva> crearReserva(ReservaDTO dto);

    /**
     * Realiza el Check-In (Pasa a estado "OCUPADA").
     */
    List<Reserva> crearOcupacion(ReservaDTO dto);


    /**
     * Elimina una reserva existente dado su ID (si se conoce).
     */
    void eliminarReserva(Integer id) throws Exception;

    /**
     * Busca y elimina una reserva activa para una habitación en una fecha específica.
     * Útil cuando el frontend no tiene el ID de la reserva.
     */
    void cancelarReservaPorFecha(Integer idHabitacion, LocalDate fecha) throws Exception;
}