package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.GrillaHabitacionDTO;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestorHabitacionTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private GestorHabitacion gestorHabitacion;

    // ==========================================================
    // TEST 1: VALIDACIONES DE FECHAS
    // ==========================================================
    @Test
    void obtenerEstado_DeberiaLanzarExcepcion_SiFechasSonNulas() {
        assertThrows(IllegalArgumentException.class, () ->
                gestorHabitacion.obtenerEstadoHabitaciones(null, LocalDate.now())
        );
    }

    @Test
    void obtenerEstado_DeberiaLanzarExcepcion_SiDesdeEsPosteriorAHasta() {
        LocalDate desde = LocalDate.now().plusDays(5);
        LocalDate hasta = LocalDate.now(); // Hasta es antes que desde

        assertThrows(IllegalArgumentException.class, () ->
                gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta)
        );
    }

    // ==========================================================
    // TEST 2: SIN RESERVAS (Todo Disponible)
    // ==========================================================
    @Test
    void obtenerEstado_SinReservas_DeberiaDevolverTodoDisponible() {
        // GIVEN
        LocalDate desde = LocalDate.of(2025, 1, 1);
        LocalDate hasta = LocalDate.of(2025, 1, 3); // 3 días

        // Creamos habitación Mock (Clase anónima porque es abstracta)
        Habitacion habMock = new Habitacion() {};
        habMock.setId(1);
        habMock.setNumero("101");
        habMock.setEstado(EstadoHabitacion.DISPONIBLE);

        when(habitacionRepository.findAll()).thenReturn(List.of(habMock));
        // No hay reservas en ese rango
        when(reservaRepository.encontrarReservasEnRango(desde, hasta)).thenReturn(Collections.emptyList());

        // ACT
        List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(desde, hasta);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size()); // 1 habitación

        // Verificamos los días
        GrillaHabitacionDTO grilla = resultado.get(0);
        assertEquals(3, grilla.getEstadosPorDia().size()); // 3 días generados

        // Todos los días deben estar DISPONIBLES
        assertTrue(grilla.getEstadosPorDia().stream()
                .allMatch(dia -> dia.getEstado() == EstadoHabitacion.DISPONIBLE));
    }

    // ==========================================================
    // TEST 3: CON RESERVA (Ocupada en el medio)
    // ==========================================================
    @Test
    void obtenerEstado_ConReserva_DeberiaMarcarDiasOcupados() {
        // GIVEN
        LocalDate rangoInicio = LocalDate.of(2025, 1, 1);
        LocalDate rangoFin = LocalDate.of(2025, 1, 3);

        // 1. La habitación "General" que devuelve el repositorio (ID 1)
        Habitacion habMock = new Habitacion() {};
        habMock.setId(1);
        habMock.setEstado(EstadoHabitacion.DISPONIBLE);

        // 2. La habitación "Interna" de la reserva
        // TRUCO: Debe tener el MISMO ID (1) y estado OCUPADA
        Habitacion habEnReserva = new Habitacion() {};
        habEnReserva.setId(1); // <--- ⭐ ESTO FALTABA: EL ID TIENE QUE COINCIDIR ⭐
        habEnReserva.setEstado(EstadoHabitacion.OCUPADA);

        // Reserva para el día 2
        Reserva reservaMock = new Reserva();
        reservaMock.setIngreso(LocalDate.of(2025, 1, 2));
        reservaMock.setEgreso(LocalDate.of(2025, 1, 2));

        // Asignamos la habitación "ocupada" a la reserva
        reservaMock.setHabitaciones(List.of(habEnReserva));

        // WHEN
        when(habitacionRepository.findAll()).thenReturn(List.of(habMock));
        when(reservaRepository.encontrarReservasEnRango(rangoInicio, rangoFin))
                .thenReturn(List.of(reservaMock));

        // ACT
        List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(rangoInicio, rangoFin);

        // ASSERT
        var estados = resultado.get(0).getEstadosPorDia();

        // Día 1: Disponible
        assertEquals(EstadoHabitacion.DISPONIBLE, estados.get(0).getEstado());
        // Día 2: OCUPADA (Ahora sí debería coincidir el ID y encontrar la reserva)
        assertEquals(EstadoHabitacion.OCUPADA, estados.get(1).getEstado());
        // Día 3: Disponible
        assertEquals(EstadoHabitacion.DISPONIBLE, estados.get(2).getEstado());
    }

    // ==========================================================
    // TEST 4: MANTENIMIENTO (Sin reservas)
    // ==========================================================
    @Test
    void obtenerEstado_Mantenimiento_DeberiaMarcarMantenimientoEnFechas() {
        // GIVEN
        LocalDate rangoInicio = LocalDate.of(2025, 5, 1);
        LocalDate rangoFin = LocalDate.of(2025, 5, 3);

        // Habitación en Mantenimiento del 1 al 2
        Habitacion habMantenimiento = new Habitacion() {};
        habMantenimiento.setId(2);
        habMantenimiento.setEstado(EstadoHabitacion.MANTENIMIENTO);
        habMantenimiento.setIngreso(LocalDate.of(2025, 5, 1)); // Inicio mant
        habMantenimiento.setEgreso(LocalDate.of(2025, 5, 2));  // Fin mant

        when(habitacionRepository.findAll()).thenReturn(List.of(habMantenimiento));
        // Sin reservas de clientes
        when(reservaRepository.encontrarReservasEnRango(any(), any())).thenReturn(Collections.emptyList());

        // ACT
        List<GrillaHabitacionDTO> resultado = gestorHabitacion.obtenerEstadoHabitaciones(rangoInicio, rangoFin);

        // ASSERT
        var estados = resultado.get(0).getEstadosPorDia();

        // Día 1: MANTENIMIENTO (coincide fecha)
        assertEquals(EstadoHabitacion.MANTENIMIENTO, estados.get(0).getEstado());
        // Día 2: MANTENIMIENTO (coincide fecha)
        assertEquals(EstadoHabitacion.MANTENIMIENTO, estados.get(1).getEstado());
        // Día 3: DISPONIBLE (fuera de fecha de mantenimiento)
        assertEquals(EstadoHabitacion.DISPONIBLE, estados.get(2).getEstado());
    }
}