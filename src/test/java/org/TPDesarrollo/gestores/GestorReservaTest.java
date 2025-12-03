package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Habitacion;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.clases.Reserva;
import org.TPDesarrollo.dtos.ReservaDTO;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.TPDesarrollo.repository.HuespedRepository;
import org.TPDesarrollo.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestorReservaTest {

    @Mock
    private HabitacionRepository habitacionRepository;
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private HuespedRepository huespedRepository;

    @InjectMocks
    private GestorReserva gestorReserva;


    @Test
    void crearReserva_DatosValidos_DeberiaGuardarReserva() {
        // GIVEN
        ReservaDTO dto = new ReservaDTO();
        dto.setIngreso(LocalDate.now().plusDays(1));
        dto.setEgreso(LocalDate.now().plusDays(5));
        dto.setHabitaciones(List.of(1));

        ReservaDTO.DatosHuespedReserva datosHuesped = new ReservaDTO.DatosHuespedReserva();
        datosHuesped.setDocumento("12345678");
        datosHuesped.setNombre("Test");
        datosHuesped.setApellido("User");
        datosHuesped.setTipoDocumento("DNI");
        dto.setHuesped(datosHuesped);


        Habitacion habitacionMock = new Habitacion() {};
        habitacionMock.setId(1);
        habitacionMock.setNumero("101");
        habitacionMock.setReservaActual(null);

        Huesped huespedMock = new Huesped();
        huespedMock.setId(10);
        huespedMock.setDocumento("12345678");

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setId(999);

        // WHEN
        when(habitacionRepository.findAllById(List.of(1))).thenReturn(List.of(habitacionMock));
        when(huespedRepository.findByDocumento("12345678")).thenReturn(huespedMock);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);

        // ACT
        Reserva resultado = gestorReserva.crearReserva(dto);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(999, resultado.getId());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }


    @Test
    void crearReserva_FechaIngresoPosteriorAEgreso_DeberiaLanzarExcepcion() {
        ReservaDTO dto = new ReservaDTO();
        dto.setIngreso(LocalDate.now());
        dto.setEgreso(LocalDate.now().minusDays(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorReserva.crearReserva(dto);
        });

        assertEquals("La fecha de ingreso debe ser anterior al egreso", exception.getMessage());
    }


    @Test
    void crearReserva_HabitacionOcupada_DeberiaLanzarExcepcion() {
        LocalDate ingreso = LocalDate.of(2025, 1, 10);
        LocalDate egreso = LocalDate.of(2025, 1, 15);

        ReservaDTO dto = new ReservaDTO();
        dto.setIngreso(ingreso);
        dto.setEgreso(egreso);
        dto.setHabitaciones(List.of(1));


        Habitacion habitacionOcupada = new Habitacion() {};
        habitacionOcupada.setId(1);
        habitacionOcupada.setNumero("101");

        Reserva reservaExistente = new Reserva();
        reservaExistente.setIngreso(LocalDate.of(2025, 1, 5));
        reservaExistente.setEgreso(LocalDate.of(2025, 1, 12));

        habitacionOcupada.setReservaActual(reservaExistente);

        when(habitacionRepository.findAllById(List.of(1))).thenReturn(List.of(habitacionOcupada));

        assertThrows(RuntimeException.class, () -> {
            gestorReserva.crearReserva(dto);
        });

        verify(reservaRepository, never()).save(any());
    }
}