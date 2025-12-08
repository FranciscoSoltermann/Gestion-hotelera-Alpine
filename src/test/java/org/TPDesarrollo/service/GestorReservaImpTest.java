package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.ReservaDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.repository.EstadiaRepository;
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
import java.util.*;

@ExtendWith(MockitoExtension.class)
class GestorReservaImpTest {

    @Mock
    private HabitacionRepository habitacionRepository;
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private HuespedRepository huespedRepository;
    @Mock
    private EstadiaRepository estadiaRepository;

    @InjectMocks
    private GestorReservaImp gestorReserva;

    private ReservaDTO validReservaDTO;
    private Habitacion habitacion1;
    private Huesped huespedTitular;
    private Reserva reservaEntity;
    private ReservaDTO.DatosHuespedReserva datosHuesped;

    @BeforeEach
    void setUp() {
        datosHuesped = new ReservaDTO.DatosHuespedReserva(
                "DNI", "12345678", "Juan", "Perez", "555-1234"
        );

        huespedTitular = Huesped.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Perez")
                .documento("12345678")
                .build();

        habitacion1 = new Habitacion();
        habitacion1.setIdHabitacion(101);
        habitacion1.setNumero(String.valueOf(101));

        validReservaDTO = ReservaDTO.builder()
                .ingreso(LocalDate.now().plusDays(1))
                .egreso(LocalDate.now().plusDays(5))
                .huesped(datosHuesped)
                .habitaciones(Collections.singletonList(101))
                .ocupantesPorHabitacion(new HashMap<>())
                .build();

        reservaEntity = new Reserva();
        reservaEntity.setIdReserva(1);
        reservaEntity.setIngreso(validReservaDTO.getIngreso());
        reservaEntity.setEgreso(validReservaDTO.getEgreso());
        reservaEntity.setHuesped(huespedTitular);
        reservaEntity.setHabitacion(habitacion1);
        reservaEntity.setEstado("RESERVADA");
    }

    /**
     * CAMINO FELIZ: Crear Reserva Exitosa.
     * Escenario: Fechas válidas, habitación existe y libre, huésped nuevo.
     */
    @Test
    void testCrearReserva_CaminoFeliz() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(validReservaDTO.getHabitaciones()))
                .thenReturn(Collections.singletonList(habitacion1));

        when(huespedRepository.findByDocumento(datosHuesped.getDocumento()))
                .thenReturn(huespedTitular);
        when(huespedRepository.save(any(Huesped.class))).thenReturn(huespedTitular);

        when(reservaRepository.encontrarSolapamientos(eq(101), any(), any()))
                .thenReturn(Collections.emptyList());

        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setIdReserva(1);
            return r;
        });

        // --- ACT ---
        List<Reserva> resultado = gestorReserva.crearReserva(validReservaDTO);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("RESERVADA", resultado.getFirst().getEstado());
        assertEquals(101, resultado.getFirst().getHabitacion().getIdHabitacion());

        // CORRECCIÓN AQUÍ: Cambiado times(1) a times(2) porque el gestor guarda al crear
        // y luego guarda otra vez al añadir ocupantes/finalizar el bucle.
        verify(reservaRepository, times(2)).save(any(Reserva.class));
        verify(estadiaRepository, never()).save(any(Estadia.class));
    }

    /**
     * CAMINO FELIZ: Crear Ocupación (Check-in) Exitosa.
     * Escenario: Similar a reserva pero estado final debe ser OCUPADA y se genera Estadía.
     */
    @Test
    void testCrearOcupacion_CaminoFeliz() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(validReservaDTO.getHabitaciones()))
                .thenReturn(Collections.singletonList(habitacion1));

        when(huespedRepository.findByDocumento(anyString())).thenReturn(huespedTitular);
        when(huespedRepository.save(any(Huesped.class))).thenReturn(huespedTitular);

        when(reservaRepository.encontrarSolapamientos(eq(101), any(), any()))
                .thenReturn(Collections.emptyList());

        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> {
            Reserva r = i.getArgument(0);
            r.setIdReserva(2);
            return r;
        });

        // --- ACT ---
        List<Reserva> resultado = gestorReserva.crearOcupacion(validReservaDTO);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals("OCUPADA", resultado.getFirst().getEstado());

        verify(estadiaRepository, times(1)).save(any(Estadia.class));
    }

    /**
     * CASO DE ERROR: Fechas inválidas.
     * Escenario: Ingreso posterior a Egreso.
     */
    @Test
    void testCrearReserva_ErrorFechas() {
        // --- ARRANGE ---
        validReservaDTO.setIngreso(LocalDate.now().plusDays(10));
        validReservaDTO.setEgreso(LocalDate.now().plusDays(5));

        // --- ACT & ASSERT ---
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                gestorReserva.crearReserva(validReservaDTO)
        );

        assertEquals("La fecha de ingreso debe ser anterior al egreso", ex.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    /**
     * CASO DE ERROR: Habitación no encontrada.
     * Escenario: El repositorio de habitaciones devuelve lista vacía.
     */
    @Test
    void testCrearReserva_ErrorHabitacionNoExiste() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        // --- ACT & ASSERT ---
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gestorReserva.crearReserva(validReservaDTO)
        );

        assertEquals("Habitación no encontrada", ex.getMessage());
    }

    /**
     * CASO DE ERROR: Habitación ya reservada (Solapamiento).
     * Escenario: Existe una reserva en esas fechas para esa habitación.
     */
    @Test
    void testCrearReserva_ErrorSolapamiento() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(anyList()))
                .thenReturn(Collections.singletonList(habitacion1));
        when(huespedRepository.findByDocumento(anyString())).thenReturn(huespedTitular);

        Reserva conflicto = new Reserva();
        conflicto.setEstado("RESERVADA");
        when(reservaRepository.encontrarSolapamientos(eq(101), any(), any()))
                .thenReturn(Collections.singletonList(conflicto));

        // --- ACT & ASSERT ---
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gestorReserva.crearReserva(validReservaDTO)
        );

        assertTrue(ex.getMessage().contains("ya reservada en esas fechas"));
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    /**
     * LOGICA COMPLEJA: Transformar Reserva a Ocupación.
     * Escenario: Check-in de alguien que YA tenía reserva previa.
     * El sistema debe detectar la reserva 'RESERVADA' del mismo huésped y actualizarla a 'OCUPADA'.
     */
    @Test
    void testCrearOcupacion_TransformarReservaExistente() {
        // --- ARRANGE ---
        when(habitacionRepository.findAllById(anyList())).thenReturn(Collections.singletonList(habitacion1));
        when(huespedRepository.findByDocumento(anyString())).thenReturn(huespedTitular);
        when(huespedRepository.save(any())).thenReturn(huespedTitular);

        Reserva reservaPrevia = new Reserva();
        reservaPrevia.setIdReserva(50);
        reservaPrevia.setHuesped(huespedTitular);
        reservaPrevia.setEstado("RESERVADA");
        reservaPrevia.setHabitacion(habitacion1);

        when(reservaRepository.encontrarSolapamientos(eq(101), any(), any()))
                .thenReturn(Collections.singletonList(reservaPrevia));

        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        // --- ACT ---
        // Llamamos a crearOcupacion (Check-in)
        List<Reserva> resultado = gestorReserva.crearOcupacion(validReservaDTO);

        // --- ASSERT ---
        assertEquals(1, resultado.size());
        Reserva r = resultado.getFirst();

        assertEquals(50, r.getIdReserva());
        assertEquals("OCUPADA", r.getEstado());

        verify(estadiaRepository).save(any(Estadia.class));
    }

    /**
     * TEST ELIMINAR RESERVA: Caso Exitoso.
     * Escenario: Borrar reserva por ID que está en estado 'RESERVADA'.
     */
    @Test
    void testEliminarReserva_Exito() throws Exception {
        // --- ARRANGE ---
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reservaEntity));
        // reservaEntity en setUp tiene estado "RESERVADA"

        // --- ACT ---
        gestorReserva.eliminarReserva(1);

        // --- ASSERT ---
        verify(reservaRepository, times(1)).delete(reservaEntity);
    }

    /**
     * TEST ELIMINAR RESERVA: Error Estado Incorrecto.
     * Escenario: Intentar borrar una reserva que ya está 'OCUPADA'.
     */
    @Test
    void testEliminarReserva_ErrorYaOcupada() {
        // --- ARRANGE ---
        reservaEntity.setEstado("OCUPADA");
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reservaEntity));

        // --- ACT & ASSERT ---
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gestorReserva.eliminarReserva(1)
        );

        assertTrue(ex.getMessage().contains("No se puede eliminar"));
        verify(reservaRepository, never()).delete(any());
    }

    /**
     * TEST CANCELAR POR FECHA: Caso Exitoso.
     * Escenario: Eliminar desde la grilla usando ID habitación y fecha.
     */
    @Test
    void testCancelarReservaPorFecha_Exito() throws Exception {
        // --- ARRANGE ---
        LocalDate fechaSeleccionada = LocalDate.now();
        when(reservaRepository.buscarReservaActivaEnFecha(101, fechaSeleccionada))
                .thenReturn(Optional.of(reservaEntity));

        // --- ACT ---
        gestorReserva.cancelarReservaPorFecha(101, fechaSeleccionada);

        // --- ASSERT ---
        verify(reservaRepository, times(1)).delete(reservaEntity);
    }

    /**
     * TEST CANCELAR POR FECHA: No encontrada.
     */
    @Test
    void testCancelarReservaPorFecha_NoEncontrada() {
        // --- ARRANGE ---
        when(reservaRepository.buscarReservaActivaEnFecha(anyInt(), any()))
                .thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThrows(RuntimeException.class, () ->
                gestorReserva.cancelarReservaPorFecha(101, LocalDate.now())
        );
    }
}