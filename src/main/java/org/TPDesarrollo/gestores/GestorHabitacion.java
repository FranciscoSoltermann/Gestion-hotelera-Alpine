package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.EstadoDiaDTO;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository; // IMPORTACIÓN AGREGADA
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; // IMPORTACIÓN AGREGADA


@Service
public class GestorHabitacion {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository; // VARIABLE AGREGADA

    @Autowired
    public GestorHabitacion(HabitacionRepository habitacionRepository, ReservaRepository reservaRepository) { // INYECCIÓN AGREGADA
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<GrillaHabitacionDTO> obtenerEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null)
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");

        if (fechaDesde.isAfter(fechaHasta))
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a hasta");

        // 1) Todas las habitaciones
        List<Habitacion> todas = habitacionRepository.findAll();

        // 2) Traer reservas solapadas
        // Asegúrate de que este método exista en tu ReservaRepository
        List<Reserva> reservasSolapadas =
                reservaRepository.encontrarReservasEnRango(fechaDesde, fechaHasta);

        // Agrupar reservas por habitación
        Map<Integer, List<Reserva>> reservasPorHab = new HashMap<>();

// 2. Recorremos las reservas
        for (Reserva r : reservasSolapadas) {

            // --- AQUÍ TIENES QUE REVISAR TU CLASE RESERVA ---
            // Si te sigue dando error en .getHabitaciones(), es porque tu lista
            // se llama diferente en Reserva.java.
            // Opciones comunes: .getListaHabitaciones(), .getItems(), .getDetalles()

            List<Habitacion> habitacionesDeEstaReserva = r.getHabitaciones();

            if (habitacionesDeEstaReserva != null) {
                for (Habitacion h : habitacionesDeEstaReserva) {

                    // 3. CORRECCIÓN: Ahora esto funciona porque el mapa espera Integer
                    reservasPorHab
                            .computeIfAbsent(h.getId(), k -> new ArrayList<>())
                            .add(r);
                }
            }
        }

        List<GrillaHabitacionDTO> resultado = new ArrayList<>();

        for (Habitacion hab : todas) {

            GrillaHabitacionDTO fila = new GrillaHabitacionDTO();
            fila.setIdHabitacion(hab.getId());
            fila.setNumero(hab.getNumero());
            fila.setTipo(hab.getClass().getSimpleName());

            List<EstadoDiaDTO> estados = new ArrayList<>();

            LocalDate dia = fechaDesde;

            while (!dia.isAfter(fechaHasta)) {

                EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE;

                List<Reserva> reservas = reservasPorHab.get(hab.getId());

                if (reservas != null) {
                    for (Reserva r : reservas) {
                        // Verificamos si el día cae dentro del rango de la reserva
                        boolean dentro =
                                !dia.isBefore(r.getFechaIngreso()) &&
                                        !dia.isAfter(r.getFechaEgreso());

                        if (dentro) {
                            estado = r.getEstadoHabitacion(); // debe venir de la reserva
                            break; // Prioridad a la primera reserva encontrada (o definir lógica de prioridad)
                        }
                    }
                }

                estados.add(new EstadoDiaDTO(dia, estado));
                dia = dia.plusDays(1);
            }

            fila.setEstadosPorDia(estados);
            resultado.add(fila);
        }

        return resultado;
    }
}