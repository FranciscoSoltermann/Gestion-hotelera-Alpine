package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Repositorio para la entidad Habitacion, proporcionando métodos
 * para operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    /**
     * Busca habitaciones por su estado.
     *
     * @param estado Estado de la habitación (DISPONIBLE, OCUPADA, RESERVADA, MANTENIMIENTO).
     * @return Lista de habitaciones que coinciden con el estado dado.
     */
    List<Habitacion> findByEstado(EstadoHabitacion estado);
    /**
     * Busca una habitación por su número único.
     *
     * @param numero Número de la habitación.
     * @return Habitación correspondiente al número dado, si existe.
     */
    Optional<Habitacion> findByNumero(String numero);
    /**
     * Busca habitaciones con una capacidad mayor o igual a la especificada.
     *
     * @param capacidad Capacidad mínima requerida.
     * @return Lista de habitaciones que cumplen con la capacidad mínima.
     */
    List<Habitacion> findByCapacidadGreaterThanEqual(Integer capacidad);
    /**
     * Busca habitaciones disponibles en un rango de fechas específico.
     *
     * @param fechaIngreso Fecha de ingreso (check-in).
     * @param fechaEgreso  Fecha de egreso (check-out).
     * @return Lista de habitaciones disponibles en el rango de fechas.
     */
    @Query("""
        SELECT h FROM Habitacion h
        WHERE h.id NOT IN (
            SELECT r.habitacion.id 
            FROM Reserva r 
            WHERE (r.ingreso < :fechaEgreso AND r.egreso > :fechaIngreso)
            AND r.estado <> 'CANCELADA' 
        )
    """)
    List<Habitacion> buscarDisponiblesEnRango(
            @Param("fechaIngreso") LocalDate fechaIngreso,
            @Param("fechaEgreso") LocalDate fechaEgreso
    );
    /**
     * Obtiene las habitaciones que están bloqueadas por mantenimiento en una fecha específica.
     *
     * @param fecha Fecha para verificar el bloqueo por mantenimiento.
     * @return Lista de habitaciones bloqueadas por mantenimiento en la fecha dada.
     */
    @Query("""
       SELECT h FROM Habitacion h
       WHERE h.ingreso IS NOT NULL 
         AND h.egreso IS NOT NULL
         AND :fecha BETWEEN h.ingreso AND h.egreso
       """)
    /**
     * Obtiene las habitaciones que están bloqueadas por mantenimiento en una fecha específica.
     *
     * @param fecha Fecha para verificar el bloqueo por mantenimiento.
     * @return Lista de habitaciones bloqueadas por mantenimiento en la fecha dada.
     */
    List<Habitacion> obtenerBloqueadasPorMantenimientoEnFecha(@Param("fecha") LocalDate fecha);
    /**
     * Cuenta la cantidad de habitaciones por estado.
     *
     * @param estado Estado de la habitación.
     * @return Cantidad de habitaciones en el estado especificado.
     */
    long countByEstado(EstadoHabitacion estado);
}