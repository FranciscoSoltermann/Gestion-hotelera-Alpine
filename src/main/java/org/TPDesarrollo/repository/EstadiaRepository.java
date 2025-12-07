package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {

    Optional<Estadia> findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(Integer idHabitacion);

    @Query("SELECT e FROM Estadia e " +
                  "JOIN FETCH e.habitacion h " +
                  "LEFT JOIN FETCH e.reserva r " +
                  "LEFT JOIN FETCH r.huesped rh " + // Aseguramos que el Huesped titular se cargue también
                  "LEFT JOIN FETCH e.itemsConsumo c " +
                  "WHERE h.idHabitacion = :idHabitacion AND e.fechaHoraEgreso IS NULL")
    Optional<Estadia> findEstadiaActivaConDetalles(@Param("idHabitacion") Integer idHabitacion);
}