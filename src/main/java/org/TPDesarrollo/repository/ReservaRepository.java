package org.TPDesarrollo.repository;

import org.TPDesarrollo.clases.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar si existe alguna reserva para ESTA habitación en ESTAS fechas
    // (Ignorando las canceladas si tuvieras ese estado)
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

    // Para el GestorHabitacion (pintar la grilla)
    @Query("SELECT r FROM Reserva r WHERE r.ingreso < :hasta AND r.egreso > :desde")
    List<Reserva> encontrarReservasEnRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}