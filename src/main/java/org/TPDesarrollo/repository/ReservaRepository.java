package org.TPDesarrollo.repository;

import org.TPDesarrollo.clases.Reserva;
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


    // Para el GestorReserva (validar solapamientos)
    // Buscar reservas que se solapen con un rango de fechas dado para una habitación específica
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.habitacion.id = :idHabitacion
        AND (r.ingreso < :egreso AND r.egreso > :ingreso)
    """)
    List<Reserva> encontrarSolapamientos(
            @Param("idHabitacion") Integer idHabitacion,
            @Param("ingreso") LocalDate ingreso,
            @Param("egreso") LocalDate egreso
    );

    @Query("SELECT r FROM Reserva r WHERE r.ingreso < :hasta AND r.egreso > :desde")
    List<Reserva> encontrarReservasEnRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}