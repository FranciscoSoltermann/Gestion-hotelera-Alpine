package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad Reserva.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas relacionadas con las reservas.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {


    @Query("SELECT r FROM Reserva r WHERE r.habitacion.id = :idHabitacion " +
            "AND r.estado <> 'CANCELADA' " +
            "AND ((r.ingreso BETWEEN :ingreso AND :egreso) " +
            "OR (r.egreso BETWEEN :ingreso AND :egreso) " +
            "OR (:ingreso BETWEEN r.ingreso AND r.egreso))")
    List<Reserva> encontrarSolapamientos(Integer idHabitacion, LocalDate ingreso, LocalDate egreso);

    @Query("SELECT r FROM Reserva r WHERE r.ingreso < :hasta AND r.egreso > :desde")
    List<Reserva> encontrarReservasEnRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}