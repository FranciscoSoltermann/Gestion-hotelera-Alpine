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

    // CORRECCIÓN: Usamos 'ingreso' y 'egreso' que son los nombres reales en tu Entidad
    @Query("SELECT r FROM Reserva r WHERE r.ingreso <= :fechaHasta AND r.egreso >= :fechaDesde")
    List<Reserva> encontrarReservasEnRango(@Param("fechaDesde") LocalDate fechaDesde,
                                           @Param("fechaHasta") LocalDate fechaHasta);
}