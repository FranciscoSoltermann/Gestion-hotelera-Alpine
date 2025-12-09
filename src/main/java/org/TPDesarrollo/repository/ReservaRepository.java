package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Repositorio para la entidad Reserva.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    /**
     * Encuentra reservas que se solapan con un rango de fechas dado para una habitación específica.
     *
     * @param idHabitacion El ID de la habitación.
     * @param ingreso      La fecha de ingreso.
     * @param egreso       La fecha de egreso.
     * @return Una lista de reservas que se solapan con el rango de fechas proporcionado.
     */
    @Query("SELECT r FROM Reserva r WHERE r.habitacion.idHabitacion = :idHabitacion " +
            "AND r.estado <> 'CANCELADA' " +
            "AND (r.ingreso < :egreso AND r.egreso > :ingreso)")
    List<Reserva> encontrarSolapamientos(
            @Param("idHabitacion") Integer idHabitacion,
            @Param("ingreso") LocalDate ingreso,
            @Param("egreso") LocalDate egreso
    );
    /**
     * Encuentra todas las reservas que se solapan con un rango de fechas dado.
     *
     * @param desde La fecha de inicio del rango.
     * @param hasta La fecha de fin del rango.
     * @return Una lista de reservas que se solapan con el rango de fechas proporcionado.
     */
    @Query("SELECT r FROM Reserva r WHERE r.ingreso < :hasta AND r.egreso > :desde")
    List<Reserva> encontrarReservasEnRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    /**
     * Busca una reserva activa para una habitación específica en una fecha dada.
     *
     * @param idHabitacion El ID de la habitación.
     * @param fecha        La fecha en la que se busca la reserva activa.
     * @return Un Optional que contiene la reserva si se encuentra, o vacío si no.
     */
    @Query("SELECT r FROM Reserva r " +
            "WHERE r.habitacion.idHabitacion = :idHabitacion " +
            "AND r.estado = 'RESERVADA' " +
            "AND (r.ingreso <= :fecha AND r.egreso > :fecha)")
    Optional<Reserva> buscarReservaActivaEnFecha(
            @Param("idHabitacion") Integer idHabitacion,
            @Param("fecha") LocalDate fecha
    );
}