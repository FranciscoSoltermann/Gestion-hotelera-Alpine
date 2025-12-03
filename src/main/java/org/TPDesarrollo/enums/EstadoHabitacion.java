package org.TPDesarrollo.enums;

/**
 * Enum que representa los posibles estados de una habitación.
 * Los estados incluyen:
 * - DISPONIBLE: La habitación está libre para ser reservada o ocupada.
 * - OCUPADA: La habitación está actualmente ocupada por un huésped.
 * - RESERVADA: La habitación ha sido reservada pero aún no está ocupada.
 * - MANTENIMIENTO: La habitación está fuera de servicio por mantenimiento o reparaciones.
 */
public enum EstadoHabitacion {
    DISPONIBLE,
    OCUPADA,
    RESERVADA,
    MANTENIMIENTO
}
