package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.mappers.HuespedMapper;
import org.TPDesarrollo.repository.HuespedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GestorHuespedImpTest {

    @Mock private HuespedRepository huespedRepository;
    @Mock private HuespedMapper huespedMapper;

    @InjectMocks
    private GestorHuespedImp gestorHuesped;

    private HuespedDTO huespedDTO;
    private Huesped huespedEntidad;

    @BeforeEach
    void setUp() {
        // Datos de prueba
        huespedDTO = new HuespedDTO();
        huespedDTO.setNombre("Lionel");
        huespedDTO.setApellido("Messi");
        huespedDTO.setDocumento("10101010");
        huespedDTO.setTipoDocumento(TipoDocumento.DNI);
        huespedDTO.setEmail("lio@test.com");

        huespedEntidad = new Huesped();
        huespedEntidad.setId(1);
        huespedEntidad.setNombre("Lionel");
        huespedEntidad.setDocumento("10101010");
    }

    @Test
    void testDarDeAltaHuesped_CaminoFeliz() {
        // --- ARRANGE ---
        // 1. Simulamos que el documento NO existe
        when(huespedRepository.existsByDocumento(huespedDTO.getDocumento())).thenReturn(false);

        // 2. Mappers
        when(huespedMapper.toEntity(huespedDTO)).thenReturn(huespedEntidad);

        // 3. Guardado
        when(huespedRepository.save(any(Huesped.class))).thenReturn(huespedEntidad);

        // --- ACT ---
        Huesped resultado = gestorHuesped.darDeAltaHuesped(huespedDTO);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals("Lionel", resultado.getNombre());
        verify(huespedRepository).save(any(Huesped.class));
    }

    @Test
    void testDarDeAltaHuesped_ErrorDniExistente() {
        // --- ARRANGE ---
        when(huespedRepository.existsByDocumento(huespedDTO.getDocumento())).thenReturn(true);

        // --- ACT & ASSERT ---
        assertThrows(DniExistente.class, () -> gestorHuesped.darDeAltaHuesped(huespedDTO));

        // Verificamos que NUNCA se guardó
        verify(huespedRepository, never()).save(any());
    }

    @Test
    void testBuscarHuespedes_Filtros() {
        // --- ARRANGE ---
        String apellido = "Messi";
        List<Huesped> listaSimulada = Collections.singletonList(huespedEntidad);

        // Simulamos la búsqueda del repo
        when(huespedRepository.buscarHuespedesPorCriterios(eq("Messi"), any(), any(), any()))
                .thenReturn(listaSimulada);

        // Simulamos el mapper response
        when(huespedMapper.toDto(huespedEntidad)).thenReturn(huespedDTO);

        // --- ACT ---
        List<HuespedDTO> resultado = gestorHuesped.buscarHuespedes(apellido, null, null, null);

        // --- ASSERT ---
        assertEquals(1, resultado.size());
        assertEquals("Lionel", resultado.get(0).getNombre());
        verify(huespedMapper, times(1)).toDto(huespedEntidad);
    }
}