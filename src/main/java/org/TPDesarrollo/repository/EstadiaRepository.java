package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Repositorio para la entidad Estadia.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas en la base de datos.
 */
@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {
    /**
     * Busca estadías activas (sin fecha de egreso) para una habitación específica.
     *
     * @param idHabitacion El ID de la habitación.
     * @return Una lista de estadías activas.
     */
    List<Estadia> findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(Integer idHabitacion);
    /**
     * Busca estadías activas (sin fecha de egreso) para una habitación específica,
     * incluyendo detalles relacionados como la habitación, reserva, huésped y items de consumo.
     *
     * @param idHabitacion El ID de la habitación.
     * @return Una lista de estadías activas con detalles relacionados.
     */
    @Query("SELECT e FROM Estadia e " +
            "JOIN FETCH e.habitacion h " +
            "LEFT JOIN FETCH e.reserva r " +
            "LEFT JOIN FETCH r.huesped rh " +
            "LEFT JOIN FETCH e.itemsConsumo c " +
            "WHERE h.idHabitacion = :idHabitacion AND e.fechaHoraEgreso IS NULL")
    List<Estadia> findEstadiaActivaConDetalles(@Param("idHabitacion") Integer idHabitacion);
    /**
     * Busca una estadía activa (sin fecha de egreso) por el número de habitación.
     *
     * @param numeroHabitacion El número de la habitación.
     * @return Una Optional que contiene la estadía activa si existe.
     */
    @Query("SELECT e FROM Estadia e WHERE e.habitacion.numero = :numeroHabitacion AND e.fechaHoraEgreso IS NULL")
    Optional<Estadia> findEstadiaActivaPorHabitacion(@Param("numeroHabitacion") String numeroHabitacion);
}