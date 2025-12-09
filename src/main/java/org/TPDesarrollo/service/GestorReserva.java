package org.TPDesarrollo.service;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.Reserva;
import java.time.LocalDate;
import java.util.List;
/**
 * Interfaz para la gestión de reservas.
 */
public interface GestorReserva {
    /**
     * Crea una reserva basada en el DTO proporcionado.
     *
     * @param dto Objeto de transferencia de datos que contiene la información de la reserva a crear.
     * @return Lista de reservas creadas.
     */
    List<Reserva> crearReserva(ReservaDTO dto);
    /**
     * Crea una ocupación basada en el DTO proporcionado.
     *
     * @param dto Objeto de transferencia de datos que contiene la información de la ocupación a crear.
     * @return Lista de reservas creadas como ocupación.
     */
    List<Reserva> crearOcupacion(ReservaDTO dto);
    /**
     * Elimina una reserva por su ID.
     *
     * @param id ID de la reserva a eliminar.
     * @throws Exception Si ocurre un error durante la eliminación.
     */
    void eliminarReserva(Integer id) throws Exception;
    /**
     * Cancela una reserva para una habitación específica en una fecha dada.
     *
     * @param idHabitacion ID de la habitación.
     * @param fecha        Fecha en la que se desea cancelar la reserva.
     * @throws Exception Si ocurre un error durante la cancelación.
     */
    void cancelarReservaPorFecha(Integer idHabitacion, LocalDate fecha) throws Exception;
}