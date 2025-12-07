package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {

    // Busca la estadía activa para una habitación (sin fecha de egreso o futura)
    @Query("SELECT e FROM Estadia e WHERE e.habitacion.numero = :numeroHabitacion AND e.fechaHoraEgreso IS NULL")
    Optional<Estadia> findEstadiaActivaPorHabitacion(@Param("numeroHabitacion") String numeroHabitacion);
}