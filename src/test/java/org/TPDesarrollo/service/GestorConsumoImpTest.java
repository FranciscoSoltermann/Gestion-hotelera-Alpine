package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.CargaConsumoDTO;
import org.TPDesarrollo.entity.Estadia;
import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.repository.ConsumoRepository;
import org.TPDesarrollo.repository.EstadiaRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GestorConsumoImpTest {

    @Mock private EstadiaRepository estadiaRepository;
    @Mock private ConsumoRepository consumoRepository;
    @Mock private HabitacionRepository habitacionRepository;

    @InjectMocks
    private GestorConsumoImp gestorConsumo;

    private CargaConsumoDTO cargaConsumoDTO;
    private Habitacion habitacion;
    private Estadia estadia;

    @BeforeEach
    void setUp() {
        habitacion = new Habitacion();
        habitacion.setIdHabitacion(1);
        habitacion.setNumero(String.valueOf(101));

        estadia = new Estadia();
        estadia.setIdestadia(10);
        estadia.setFechaHoraIngreso(LocalDateTime.now().minusDays(1));
        estadia.setHabitacion(habitacion);

        cargaConsumoDTO = new CargaConsumoDTO();
        cargaConsumoDTO.setNumeroHabitacion("101");
        cargaConsumoDTO.setDescripcion("Coca Cola");
        cargaConsumoDTO.setCantidad(2);
        cargaConsumoDTO.setPrecioUnitario(1500f);
    }

    @Test
    void testCargarConsumo_Exito() {
        // ARRANGE
        when(habitacionRepository.findByNumero("101")).thenReturn(Optional.of(habitacion));
        when(estadiaRepository.findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(1))
                .thenReturn(Collections.singletonList(estadia));

        // ACT
        gestorConsumo.cargarConsumo(cargaConsumoDTO);

        // ASSERT
        verify(consumoRepository, times(1)).save(argThat(consumo ->
                consumo.getDescripcion().equals("Coca Cola") &&
                        consumo.getCantidad() == 2 &&
                        consumo.getEstadia().getIdestadia() == 10
        ));
    }

    @Test
    void testCargarConsumo_HabitacionNoExiste() {
        when(habitacionRepository.findByNumero("999")).thenReturn(Optional.empty());

        cargaConsumoDTO.setNumeroHabitacion("999");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gestorConsumo.cargarConsumo(cargaConsumoDTO)
        );

        assertTrue(ex.getMessage().contains("no existe"));
        verify(consumoRepository, never()).save(any());
    }

    @Test
    void testCargarConsumo_SinEstadiaActiva() {
        when(habitacionRepository.findByNumero("101")).thenReturn(Optional.of(habitacion));
        when(estadiaRepository.findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(1))
                .thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gestorConsumo.cargarConsumo(cargaConsumoDTO)
        );

        assertTrue(ex.getMessage().contains("no tiene estadía activa"));
        verify(consumoRepository, never()).save(any());
    }
}