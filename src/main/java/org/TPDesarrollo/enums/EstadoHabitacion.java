package org.TPDesarrollo.enums;

/**
 * Define los posibles estados de una habitación dentro del sistema de gestión hotelera.
 */
public enum EstadoHabitacion {
    /**
     * La habitación está libre, limpia y lista para ser reservada o asignada inmediatamente (Check-in).
     */
    DISPONIBLE,

    /**
     * La habitación está actualmente ocupada por uno o más huéspedes (Check-out aún no realizado).
     */
    OCUPADA,

    /**
     * La habitación tiene una reserva confirmada para una fecha futura, pero aún no está ocupada.
     */
    RESERVADA,

    /**
     * La habitación está fuera de servicio temporalmente por tareas de limpieza profunda,
     * reparaciones o mantenimiento, y no puede ser reservada ni ocupada.
     */
    MANTENIMIENTO
}