package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
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

    @Mock private HabitacionRepository habitacionRepository;
    @Mock private ReservaRepository reservaRepository;
    @Mock private HabitacionMapper habitacionMapper;

    @InjectMocks
    private GestorHabitacionImp gestorHabitacion;

    private Habitacion habitacion;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        habitacion = new Habitacion();
        habitacion.setId(1);
        habitacion.setNumero("101");
        habitacion.setCapacidad(2);
        habitacion.setEstado(EstadoHabitacion.DISPONIBLE);

        reserva = Reserva.builder()
                .habitacion(habitacion)
                .ingreso(LocalDate.now())
                .egreso(LocalDate.now().plusDays(2))
                .build();
    }

    @Test
    void testObtenerEstadoHabitaciones_GeneraGrillaCorrectamente() {

        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(3);

        when(habitacionRepository.findAll()).thenReturn(Collections.singletonList(habitacion));

        when(reservaRepository.encontrarReservasEnRango(desde, hasta))
                .thenReturn(Collections.singletonList(reserva));

        GrillaHabitacionDTO dtoMock = new GrillaHabitacionDTO();
        dtoMock.setIdHabitacion(1);
        dtoMock.setNumero("101");
        when(habitacionMapper.toGrillaDto(habitacion)).thenReturn(dtoMock);

        List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        GrillaHabitacionDTO fila = resultado.get(0);
        assertEquals(1, fila.getIdHabitacion());

        List<?> estados = fila.getEstadosPorDia();
        assertEquals(4, estados.size());
    }

    @Test
    void testObtenerEstadoHabitaciones_FechasNulas() {
        assertThrows(IllegalArgumentException.class,
                () -> gestorHabitacion.obtenerEstadoHabitaciones(null, null));
    }
}