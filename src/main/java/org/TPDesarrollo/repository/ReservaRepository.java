package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // ESTRICTA: Para evitar solapamientos al crear (la que arreglamos antes)
    @Query("SELECT r FROM Reserva r WHERE r.habitacion.idHabitacion = :idHabitacion " +
            "AND r.estado <> 'CANCELADA' " +
            "AND (r.ingreso < :egreso AND r.egreso > :ingreso)")
    List<Reserva> encontrarSolapamientos(
            @Param("idHabitacion") Integer idHabitacion,
            @Param("ingreso") LocalDate ingreso,
            @Param("egreso") LocalDate egreso
    );

    @Query("SELECT r FROM Reserva r WHERE r.ingreso < :hasta AND r.egreso > :desde")
    List<Reserva> encontrarReservasEnRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // --- NUEVO MÉTODO PARA ELIMINAR ---
    // Esta query es INCLUSIVA (<=). Busca una reserva que esté activa en la fecha específica que clickeaste.
    // Sirve para encontrar la reserva del día 12 aunque empiece el 12.
    @Query("SELECT r FROM Reserva r " +
            "WHERE r.habitacion.idHabitacion = :idHabitacion " +
            "AND r.estado = 'RESERVADA' " +
            "AND (r.ingreso <= :fecha AND r.egreso > :fecha)")
    Optional<Reserva> buscarReservaActivaEnFecha(
            @Param("idHabitacion") Integer idHabitacion,
            @Param("fecha") LocalDate fecha
    );
}