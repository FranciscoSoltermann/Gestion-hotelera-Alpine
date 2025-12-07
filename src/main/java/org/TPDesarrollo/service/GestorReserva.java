package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.Reserva;
import java.util.List;

public interface GestorReserva {

    /**
     * Crea reservas en estado "RESERVADA".
     * Soporta múltiples habitaciones.
     */
    List<Reserva> crearReserva(ReservaDTO dto);

    /**
     * Realiza el Check-In (Pasa a estado "OCUPADA").
     * Si ya existía una reserva previa para ese titular, la actualiza.
     * Si no, crea una nueva (Walk-in).
     */
    List<Reserva> crearOcupacion(ReservaDTO dto);
}