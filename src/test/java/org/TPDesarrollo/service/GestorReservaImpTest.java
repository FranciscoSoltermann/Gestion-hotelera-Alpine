package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.entity.Reserva;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class GestorReservaImpTest {

    @Mock private HabitacionRepository habitacionRepository;
    @Mock private ReservaRepository reservaRepository;
    @Mock private HuespedRepository huespedRepository;

    @InjectMocks
    private GestorReservaImp gestorReserva;

    private ReservaDTO reservaDTO;
    private Habitacion habitacion;

    @BeforeEach
    void setUp() {
        habitacion = new Habitacion();
        habitacion.setId(1);
        habitacion.setNumero("101");
        habitacion.setEstado(EstadoHabitacion.DISPONIBLE);

        reservaDTO = new ReservaDTO();
        reservaDTO.setIngreso(LocalDate.now().plusDays(1));
        reservaDTO.setEgreso(LocalDate.now().plusDays(5));
        reservaDTO.setHabitaciones(Collections.singletonList(1));

        ReservaDTO.DatosHuespedReserva datos = new ReservaDTO.DatosHuespedReserva();
        datos.setDocumento("123");
        reservaDTO.setHuesped(datos);
    }

    @Test
    void testCrearOcupacion_ActualizaHabitacion() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(any())).thenReturn(Collections.singletonList(habitacion));
        when(huespedRepository.findByDocumento(any())).thenReturn(null); // Crea nuevo huesped
        when(reservaRepository.encontrarSolapamientos(anyInt(), any(), any())).thenReturn(Collections.emptyList());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);

        // --- ACT ---
        // Simulamos un Check-in (Ocupar)
        gestorReserva.crearOcupacion(reservaDTO);

        // --- ASSERT ---
        assertEquals(EstadoHabitacion.OCUPADA, habitacion.getEstado());
        verify(habitacionRepository).save(habitacion);

        // Verificamos que se guardó la reserva
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void testCrearReserva_NoTocaHabitacion() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(any())).thenReturn(Collections.singletonList(habitacion));
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // --- ACT ---
        gestorReserva.crearReserva(reservaDTO);

        // --- ASSERT ---
        // El estado debe seguir siendo DISPONIBLE (u original)
        assertEquals(EstadoHabitacion.DISPONIBLE, habitacion.getEstado());
        // NO se debe guardar la habitación
        verify(habitacionRepository, never()).save(habitacion);
    }
}