package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.repository.HuespedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorHuespedTest {

    @Mock
    private HuespedRepository huespedRepository;


    @InjectMocks
    private GestorHuesped gestorHuesped;

    @Test
    void darDeAlta_DeberiaLanzarExcepcion_SiDniExiste() {
        // Given
        HuespedDTO dto = new HuespedDTO();
        dto.setDocumento("11122233");

        // Simulamos que el repositorio dice "sí, ya existe este documento"
        when(huespedRepository.existsByDocumento("11122233")).thenReturn(true);

        // When & Then
        assertThrows(DniExistente.class, () -> gestorHuesped.darDeAltaHuesped(dto));

        // Verificamos que NUNCA se intente guardar
        verify(huespedRepository, never()).save(any());
    }

    @Test
    void darDeAlta_DeberiaGuardar_SiDatosSonValidos() {
        // Given
        HuespedDTO dto = new HuespedDTO();
        dto.setDocumento("99999999");
        // Ponemos datos mínimos para que no falle el builder interno del Gestor
        dto.setNombre("Test");
        dto.setApellido("Test");
        dto.setPosicionIVA("Consumidor_Final");

        // Simulamos que NO existe el documento
        when(huespedRepository.existsByDocumento("99999999")).thenReturn(false);

        // Simulamos que el repositorio guarda correctamente
        // (Devolvemos un objeto Huesped cualquiera para que no dé error)
        when(huespedRepository.save(any(Huesped.class))).thenReturn(new Huesped());

        // When
        gestorHuesped.darDeAltaHuesped(dto);

        // Then
        // Verificamos que se llamó al método save del repositorio 1 vez
        verify(huespedRepository, times(1)).save(any(Huesped.class));
    }
}