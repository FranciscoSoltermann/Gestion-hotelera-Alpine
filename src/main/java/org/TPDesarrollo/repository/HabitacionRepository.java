package org.TPDesarrollo.repository;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {


    List<Habitacion> findByEstado(EstadoHabitacion estado);

    Habitacion findByNumero(String numero);

    List<Habitacion> findByCapacidadGreaterThanEqual(Integer capacidad);


    /**
     * Encuentra habitaciones que NO tengan ninguna reserva que se solape
     * con el rango de fechas solicitado.
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


    @Query("SELECT h FROM Habitacion h WHERE TYPE(h) = :clazz")
    <T extends Habitacion> List<T> findByTipo(Class<T> clazz);



    // Esto sirve para ver bloqueos administrativos (ej. Mantenimiento)
    @Query("""
       SELECT h FROM Habitacion h
       WHERE h.ingreso IS NOT NULL 
         AND h.egreso IS NOT NULL
         AND :fecha BETWEEN h.ingreso AND h.egreso
       """)
    List<Habitacion> obtenerBloqueadasPorMantenimientoEnFecha(@Param("fecha") LocalDate fecha);

    // Contadores útiles
    long countByEstado(EstadoHabitacion estado);
}