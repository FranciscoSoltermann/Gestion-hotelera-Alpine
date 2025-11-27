package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.exceptions.CuitExistente;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.repository.HuespedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorHuespedTest {

    @Mock
    private HuespedRepository huespedRepository;

    @InjectMocks
    private GestorHuesped gestorHuesped;

    // --- TEST 1: ERROR DNI (El que ya tenías) ---
    @Test
    void darDeAlta_DeberiaLanzarExcepcion_SiDniExiste() {
        // Given
        HuespedDTO dto = new HuespedDTO();
        dto.setDocumento("11122233");

        // When
        when(huespedRepository.existsByDocumento("11122233")).thenReturn(true);

        // Then
        assertThrows(DniExistente.class, () -> gestorHuesped.darDeAltaHuesped(dto));
        verify(huespedRepository, never()).save(any());
    }

    // --- TEST 2: ERROR CUIT (Este faltaba) ---
    @Test
    void darDeAlta_DeberiaLanzarExcepcion_SiCuitExiste() {
        // Given
        HuespedDTO dto = new HuespedDTO();
        dto.setDocumento("99999999");
        dto.setCuit("20-99999999-0"); // CUIT que ya existe

        // When
        when(huespedRepository.existsByDocumento(anyString())).thenReturn(false); // DNI OK
        when(huespedRepository.existsByCuit(dto.getCuit())).thenReturn(true);     // CUIT Duplicado

        // Then
        assertThrows(CuitExistente.class, () -> gestorHuesped.darDeAltaHuesped(dto));
        verify(huespedRepository, never()).save(any());
    }

    // --- TEST 3: GUARDADO EXITOSO (Mejorado) ---
    @Test
    void darDeAlta_DeberiaGuardar_SiDatosSonValidos() {
        // Given
        HuespedDTO dto = new HuespedDTO();
        dto.setDocumento("88888888");
        dto.setNombre("Test");
        dto.setApellido("Test");
        dto.setPosicionIVA(RazonSocial.Responsable_Inscripto); // IVA Explícito
        dto.setCuit("20-88888888-1");

        // When
        when(huespedRepository.existsByDocumento(anyString())).thenReturn(false);
        when(huespedRepository.existsByCuit(anyString())).thenReturn(false);
        when(huespedRepository.save(any(Huesped.class))).thenReturn(new Huesped());

        // Act
        gestorHuesped.darDeAltaHuesped(dto);

        // Then
        verify(huespedRepository, times(1)).save(any(Huesped.class));
    }

    // --- TEST 4: LOGICA AUTOMÁTICA (Consumidor Final) ---
    @Test
    void darDeAlta_DeberiaAsignarConsumidorFinal_SiNoHayCuit() {
        // Given
        HuespedDTO dto = new HuespedDTO();
        dto.setDocumento("77777777");
        dto.setCuit(null); // SIN CUIT -> El gestor debería poner Consumidor Final
        dto.setPosicionIVA(null);

        // When
        when(huespedRepository.existsByDocumento(anyString())).thenReturn(false);
        when(huespedRepository.save(any(Huesped.class))).thenReturn(new Huesped());

        // Act
        gestorHuesped.darDeAltaHuesped(dto);

        // Then
        // Verificamos que al guardar, el objeto tenga Consumidor_Final
        verify(huespedRepository).save(argThat(h ->
                h.getPosicionIVA() == RazonSocial.Consumidor_Final
        ));
    }

    // --- TEST 5: BÚSQUEDA (Este faltaba) ---
    @Test
    void buscarHuespedes_DeberiaLlamarAlRepositorio() {
        // Given
        when(huespedRepository.buscarHuespedesPorCriterios(any(), any(), any(), any()))
                .thenReturn(List.of(new Huesped())); // Devuelve lista con 1 elemento

        // Act
        var resultados = gestorHuesped.buscarHuespedes("Perez", null, null, null);

        // Then
        assertFalse(resultados.isEmpty());
        verify(huespedRepository).buscarHuespedesPorCriterios(eq("Perez"), any(), any(), any());
    }
}