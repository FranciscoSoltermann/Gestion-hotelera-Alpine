package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestorReserva {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private GestorHuesped gestorHuesped;

    @Transactional
    public void registrarReserva(ReservaDTO dto) {

        // 1. Obtener o Crear el Huesped
        Huesped huesped = gestorHuesped.buscarOCrearHuesped(dto);

        // 2. Recorrer la lista de IDs de habitaciones
        for (Long idHabitacionLong : dto.getIdHabitaciones()) {

            // Convertimos Long a Integer (porque tus repositorios usan Integer)
            Integer idHabitacion = idHabitacionLong.intValue();

            // 3. Buscar la habitación
            Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada ID: " + idHabitacion));

            // 4. Crear la Reserva (Usando SETTERS en lugar de Builder para evitar errores)
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(dto.getFechaInicio());
            reserva.setFechaFin(dto.getFechaFin());
            reserva.setHabitacion(habitacion);
            reserva.setHuesped(huesped);

            // 5. Guardar
            reservaRepository.save(reserva);
        }
    }
}