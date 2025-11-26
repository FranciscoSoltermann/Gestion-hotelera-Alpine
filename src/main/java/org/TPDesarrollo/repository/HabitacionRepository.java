package org.TPDesarrollo.repository;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    // --------------------------------------------------------------------
    // 🔍 BUSQUEDAS BÁSICAS
    // --------------------------------------------------------------------

    // Buscar por estado (DISPONIBLE, OCUPADA, RESERVADA, etc.)
    List<Habitacion> findByEstado(EstadoHabitacion estado);

    // Buscar por número de habitación (único)
    Habitacion findByNumero(String numero);

    // Traer habitaciones dentro de un rango de capacidad
    List<Habitacion> findByCapacidadGreaterThanEqual(Integer capacidad);

    // --------------------------------------------------------------------
    // 🔍 BUSQUEDAS RELACIONADAS A RESERVA ACTUAL
    // --------------------------------------------------------------------

    // Trae habitaciones que actualmente tengan una reserva asignada
    List<Habitacion> findByReservaActualIsNotNull();

    // Trae habitaciones sin reserva asignada
    List<Habitacion> findByReservaActualIsNull();

    // --------------------------------------------------------------------
    // 🔍 CONSULTAS PERSONALIZADAS
    // --------------------------------------------------------------------

    // Trae todas las habitaciones disponibles en una fecha exacta
    @Query("""
           SELECT h
           FROM Habitacion h
           WHERE h.ingreso IS NULL 
              OR h.egreso IS NULL
              OR :fecha < h.ingreso
              OR :fecha > h.egreso
           """)
    List<Habitacion> obtenerDisponiblesEnFecha(LocalDate fecha);

    // Trae habitaciones ocupadas en un día
    @Query("""
           SELECT h
           FROM Habitacion h
           WHERE h.ingreso IS NOT NULL
             AND h.egreso IS NOT NULL
             AND :fecha BETWEEN h.ingreso AND h.egreso
           """)
    List<Habitacion> obtenerOcupadasEnFecha(LocalDate fecha);

    // Habitación libre en rango de fechas (versión simple)
    @Query("""
           SELECT h
           FROM Habitacion h
           WHERE h.ingreso IS NULL OR h.egreso IS NULL
              OR (h.egreso < :desde OR h.ingreso > :hasta)
           """)
    List<Habitacion> obtenerDisponiblesEntre(LocalDate desde, LocalDate hasta);

    // --------------------------------------------------------------------
    // 🔍 CONTADORES ÚTILES
    // --------------------------------------------------------------------

    long countByEstado(EstadoHabitacion estado);

    // Habitaciones que pertenecen a un tipo específico (por nombre de clase)
    @Query("""
           SELECT h
           FROM Habitacion h
           WHERE TYPE(h) = :clazz
           """)
    <T extends Habitacion> List<T> findByTipo(Class<T> clazz);

    @Query("""
    SELECT h FROM Habitacion h
    WHERE h.ingreso IS NOT NULL AND h.egreso IS NOT NULL
    AND (
        (h.ingreso <= :fechaHasta AND h.egreso >= :fechaDesde)
    )
""")
    List<Habitacion> findHabitacionesConSolapamientos(
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

}
