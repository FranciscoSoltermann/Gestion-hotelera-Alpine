package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.EstadoDiaDTO;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar las habitaciones y sus estados.
 * Proporciona funcionalidades para obtener el estado de las habitaciones
 * en un rango de fechas determinado.
 * Implementa la lógica de negocio relacionada con las habitaciones.
 * Utiliza repositorios para acceder a los datos de habitaciones y reservas.
 * Los métodos están anotados con @Transactional
 * para gestionar las transacciones de la base de datos.
 * Los estados posibles de las habitaciones incluyen DISPONIBLE,
 * OCUPADO, RESERVADO y MANTENIMIENTO.
 * El servicio valida las fechas de entrada y salida
 * para asegurar que sean correctas antes de procesar la solicitud.
 * Devuelve una lista de DTOs que representan el estado diario
 * de cada habitación en el rango especificado.
 */
@Service
public class GestorHabitacion {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public GestorHabitacion(HabitacionRepository habitacionRepository, ReservaRepository reservaRepository) {
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    /**
     * Obtiene el estado de todas las habitaciones en un rango de fechas.
     * @param fechaDesde Fecha de inicio del rango.
     * @param fechaHasta Fecha de fin del rango.
     * @return Lista de GrillaHabitacionDTO con el estado diario de cada habitación.
     * @throws IllegalArgumentException Si las fechas son nulas o inválidas.
     */
    public List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null)
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");

        if (fechaDesde.isAfter(fechaHasta))
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a hasta");

        List<Habitacion> todas = habitacionRepository.findAll();
        List<Reserva> reservasSolapadas = reservaRepository.encontrarReservasEnRango(fechaDesde, fechaHasta);

        // Agrupar reservas por ID de habitación para acceso rápido
        Map<Integer, List<Reserva>> reservasPorHab = new HashMap<>();

        for (Reserva r : reservasSolapadas) {
            Habitacion h = r.getHabitacion();

            if (h != null) {
                reservasPorHab
                        .computeIfAbsent(h.getId(), k -> new ArrayList<>())
                        .add(r);
            }
        }

        List<GrillaHabitacionDTO> resultado = new ArrayList<>();

        for (Habitacion hab : todas) {

            GrillaHabitacionDTO fila = new GrillaHabitacionDTO();
            fila.setIdHabitacion(hab.getId());
            fila.setNumero(hab.getNumero());
            fila.setTipo(hab.getClass().getSimpleName());
            fila.setCapacidad(hab.getCapacidad());
            List<EstadoDiaDTO> estados = new ArrayList<>();
            LocalDate dia = fechaDesde;
            while (!dia.isAfter(fechaHasta)) {

                EstadoHabitacion estadoDelDia = EstadoHabitacion.DISPONIBLE;

                List<Reserva> reservas = reservasPorHab.get(hab.getId());

                if (reservas != null) {
                    for (Reserva r : reservas) {
                        boolean dentro = !dia.isBefore(r.getFechaIngreso()) && dia.isBefore(r.getFechaEgreso());
                        if (dentro) {
                            estadoDelDia = r.getEstadoHabitacion();
                            break;
                        }
                    }
                }
                if (estadoDelDia == EstadoHabitacion.DISPONIBLE) {

                    if (hab.getEstado() != EstadoHabitacion.DISPONIBLE) {
                        if (hab.getIngreso() != null && hab.getEgreso() != null) {
                            if (!dia.isBefore(hab.getIngreso()) && dia.isBefore(hab.getEgreso())) {
                                estadoDelDia = hab.getEstado();
                            }
                        } else {
                            estadoDelDia = hab.getEstado();
                        }
                    }
                }

                estados.add(new EstadoDiaDTO(dia, estadoDelDia));
                dia = dia.plusDays(1);
            }

            fila.setEstadosPorDia(estados);
            resultado.add(fila);
        }

        return resultado;
    }
}