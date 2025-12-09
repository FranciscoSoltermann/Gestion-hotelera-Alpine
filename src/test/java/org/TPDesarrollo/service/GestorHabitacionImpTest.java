package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.EstadoDiaDTO;
import org.TPDesarrollo.dto.GrillaHabitacionDTO;
import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.entity.Reserva;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.mappers.HabitacionMapper;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GestorHabitacionImpTest {

    @Mock
    private HabitacionRepository habitacionRepository;
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private HabitacionMapper habitacionMapper;

    @InjectMocks
    private GestorHabitacionImp gestorHabitacion;

    private Habitacion habitacion101;
    private GrillaHabitacionDTO grillaDTO;

    @BeforeEach
    void setUp() {
        habitacion101 = new Habitacion();
        habitacion101.setIdHabitacion(1);
        habitacion101.setNumero(String.valueOf(101));
        habitacion101.setEstado(EstadoHabitacion.DISPONIBLE);

        grillaDTO = new GrillaHabitacionDTO();
        grillaDTO.setIdHabitacion(1);
        grillaDTO.setNumero(String.valueOf(101));
    }

    /**
     * Caso 1: Habitación Libre sin Reservas.
     * Resultado: Todos los días DISPONIBLE.
     */
    @Test
    void testObtenerEstadoHabitaciones_TodoLibre() {
        LocalDate desde = LocalDate.of(2023, 1, 1);
        LocalDate hasta = LocalDate.of(2023, 1, 3);

        when(habitacionRepository.findAll()).thenReturn(Collections.singletonList(habitacion101));
        when(reservaRepository.encontrarReservasEnRango(desde, hasta)).thenReturn(Collections.emptyList());
        when(habitacionMapper.toGrillaDto(habitacion101)).thenReturn(grillaDTO);

        List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta);

        assertEquals(1, resultado.size());
        List<EstadoDiaDTO> estados = resultado.get(0).getEstadosPorDia();
        assertEquals(3, estados.size());

        assertTrue(estados.stream().allMatch(e -> e.getEstado() == EstadoHabitacion.DISPONIBLE));
    }

    /**
     * Caso 2: Habitación con Reserva ("RESERVADA").
     * Escenario: Reserva el día 2. Día 1 y 3 libres.
     */
    @Test
    void testObtenerEstadoHabitaciones_ConReserva() {
        LocalDate desde = LocalDate.of(2023, 1, 1);
        LocalDate hasta = LocalDate.of(2023, 1, 3);

        Reserva reserva = new Reserva();
        reserva.setHabitacion(habitacion101);
        reserva.setEstado("RESERVADA");
        reserva.setIngreso(LocalDate.of(2023, 1, 2));
        reserva.setEgreso(LocalDate.of(2023, 1, 3));

        when(habitacionRepository.findAll()).thenReturn(Collections.singletonList(habitacion101));
        when(reservaRepository.encontrarReservasEnRango(desde, hasta)).thenReturn(Collections.singletonList(reserva));
        when(habitacionMapper.toGrillaDto(habitacion101)).thenReturn(grillaDTO);

        List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta);
        List<EstadoDiaDTO> estados = resultado.get(0).getEstadosPorDia();

        assertEquals(EstadoHabitacion.DISPONIBLE, estados.get(0).getEstado());
        assertEquals(EstadoHabitacion.RESERVADA, estados.get(1).getEstado());
        assertEquals(EstadoHabitacion.DISPONIBLE, estados.get(2).getEstado());
    }

    /**
     * Caso 3: Habitación Ocupada ("OCUPADA").
     * Escenario: Check-in realizado.
     */
    @Test
    void testObtenerEstadoHabitaciones_Ocupada() {
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now();

        Reserva reserva = new Reserva();
        reserva.setHabitacion(habitacion101);
        reserva.setEstado("OCUPADA");
        reserva.setIngreso(desde);
        reserva.setEgreso(desde.plusDays(1));

        when(habitacionRepository.findAll()).thenReturn(Collections.singletonList(habitacion101));
        when(reservaRepository.encontrarReservasEnRango(desde, hasta)).thenReturn(Collections.singletonList(reserva));
        when(habitacionMapper.toGrillaDto(habitacion101)).thenReturn(grillaDTO);

        List<GrillaHabitacionDTO> res = gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta);

        assertEquals(EstadoHabitacion.OCUPADA, res.get(0).getEstadosPorDia().get(0).getEstado());
    }

    /**
     * Caso 4: Habitación en Mantenimiento por rango de fechas.
     * Escenario: No hay reservas, pero la habitación tiene un estado propio de mantenimiento en esas fechas.
     */
    @Test
    void testObtenerEstadoHabitaciones_Mantenimiento() {
        LocalDate desde = LocalDate.of(2023, 5, 1);
        LocalDate hasta = LocalDate.of(2023, 5, 1);

        habitacion101.setEstado(EstadoHabitacion.MANTENIMIENTO);
        habitacion101.setIngreso(LocalDate.of(2023, 4, 30));
        habitacion101.setEgreso(LocalDate.of(2023, 5, 5));

        when(habitacionRepository.findAll()).thenReturn(Collections.singletonList(habitacion101));
        when(reservaRepository.encontrarReservasEnRango(any(), any())).thenReturn(Collections.emptyList());
        when(habitacionMapper.toGrillaDto(habitacion101)).thenReturn(grillaDTO);

        List<GrillaHabitacionDTO> res = gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta);

        assertEquals(EstadoHabitacion.MANTENIMIENTO, res.get(0).getEstadosPorDia().get(0).getEstado());
    }

    /**
     * Caso Error: Fechas inválidas.
     */
    @Test
    void testObtenerEstadoHabitaciones_ErrorFechas() {
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta)
        );
    }
}