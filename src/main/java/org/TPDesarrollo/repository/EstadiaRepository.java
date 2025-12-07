package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {

    // Método básico para buscar estadía activa por ID de habitación
    Optional<Estadia> findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(Integer idHabitacion);

    // Consulta optimizada con JOIN FETCH para traer todos los datos necesarios de una vez
    @Query("SELECT e FROM Estadia e " +
            "JOIN FETCH e.habitacion h " +
            "LEFT JOIN FETCH e.reserva r " +
            "LEFT JOIN FETCH r.huesped rh " +
            "LEFT JOIN FETCH e.itemsConsumo c " +
            "WHERE h.idHabitacion = :idHabitacion AND e.fechaHoraEgreso IS NULL")
    Optional<Estadia> findEstadiaActivaConDetalles(@Param("idHabitacion") Integer idHabitacion);

    @Query("SELECT e FROM Estadia e WHERE e.habitacion.numero = :numeroHabitacion AND e.fechaHoraEgreso IS NULL")
    Optional<Estadia> findEstadiaActivaPorHabitacion(@Param("numeroHabitacion") String numeroHabitacion);
}