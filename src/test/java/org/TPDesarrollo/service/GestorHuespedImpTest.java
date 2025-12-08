package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.HuespedDTO;
import org.TPDesarrollo.entity.Huesped;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento;
import org.TPDesarrollo.exceptions.CuitExistente;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.mappers.HuespedMapper;
import org.TPDesarrollo.repository.HuespedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GestorHuespedImpTest {

    @Mock
    private HuespedRepository huespedRepository;

    @Mock
    private HuespedMapper huespedMapper;

    @InjectMocks
    private GestorHuespedImp gestorHuesped;

    private Huesped huespedEntity;
    private HuespedDTO huespedDTO;

    @BeforeEach
    void setUp() {
        huespedEntity = new Huesped();
        huespedEntity.setId(1);
        huespedEntity.setNombre("Carlos");
        huespedEntity.setApellido("Lopez");
        huespedEntity.setDocumento("12345678");
        huespedEntity.setTipoDocumento(TipoDocumento.DNI);

        huespedDTO = new HuespedDTO();
        huespedDTO.setId(1);
        huespedDTO.setNombre("Carlos");
        huespedDTO.setApellido("Lopez");
        huespedDTO.setDocumento("12345678");
        huespedDTO.setTipoDocumento(TipoDocumento.DNI);
    }

    /**
     * Test Búsqueda por DNI: Éxito.
     */
    @Test
    void testBuscarPorDni_Exito() {
        when(huespedRepository.findByDocumento("12345678")).thenReturn(huespedEntity);
        when(huespedMapper.toDto(huespedEntity)).thenReturn(huespedDTO);

        HuespedDTO resultado = gestorHuesped.buscarPorDni("12345678");

        assertNotNull(resultado);
        assertEquals("12345678", resultado.getDocumento());
    }

    /**
     * Test Búsqueda con Filtros: Verifica que se llamen al repo con los parámetros limpios.
     */
    @Test
    void testBuscarHuespedes_Filtros() {
        // Inputs sucios (espacios extra)
        String apellido = "  Lopez  ";
        String nombre = "";
        String tipoDoc = "DNI";
        String doc = " 123 ";

        when(huespedRepository.buscarHuespedesPorCriterios(eq("Lopez"), isNull(), eq(TipoDocumento.DNI), eq("123")))
                .thenReturn(Collections.singletonList(huespedEntity));
        when(huespedMapper.toDto(huespedEntity)).thenReturn(huespedDTO);

        List<HuespedDTO> resultados = gestorHuesped.buscarHuespedes(apellido, nombre, tipoDoc, doc);

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        verify(huespedRepository).buscarHuespedesPorCriterios(eq("Lopez"), isNull(), eq(TipoDocumento.DNI), eq("123"));
    }

    /**
     * Test Alta Huésped: Camino Feliz.
     */
    @Test
    void testDarDeAltaHuesped_Exito() {
        when(huespedRepository.existsByDocumento(huespedDTO.getDocumento())).thenReturn(false);
        // Sin CUIT -> Consumidor Final
        when(huespedMapper.toEntity(huespedDTO)).thenReturn(huespedEntity);
        when(huespedRepository.save(huespedEntity)).thenReturn(huespedEntity);

        gestorHuesped.darDeAltaHuesped(huespedDTO);

        assertEquals(RazonSocial.Consumidor_Final, huespedDTO.getPosicionIVA()); // Verifica regla de negocio
        verify(huespedRepository).save(huespedEntity);
    }

    /**
     * Test Alta Huésped: Error DNI Duplicado.
     */
    @Test
    void testDarDeAltaHuesped_ErrorDniExistente() {
        when(huespedRepository.existsByDocumento(huespedDTO.getDocumento())).thenReturn(true);

        assertThrows(DniExistente.class, () -> gestorHuesped.darDeAltaHuesped(huespedDTO));

        verify(huespedRepository, never()).save(any());
    }

    /**
     * Test Alta Huésped: Error CUIT Duplicado.
     */
    @Test
    void testDarDeAltaHuesped_ErrorCuitExistente() {
        huespedDTO.setCuit("20-12345678-9");
        when(huespedRepository.existsByDocumento(anyString())).thenReturn(false);
        when(huespedRepository.existsByCuit("20-12345678-9")).thenReturn(true);

        assertThrows(CuitExistente.class, () -> gestorHuesped.darDeAltaHuesped(huespedDTO));
    }

    /**
     * Test Eliminar: Error Integridad Referencial (Tiene reservas).
     */
    @Test
    void testEliminarHuesped_ErrorIntegridad() {
        when(huespedRepository.existsById(1)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("Constraint violation"))
                .when(huespedRepository).deleteById(1);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gestorHuesped.eliminarHuesped(1));

        assertTrue(ex.getMessage().contains("tiene Reservas o Estadías asociadas"));
    }

    /**
     * Test Eliminar: No existe ID.
     */
    @Test
    void testEliminarHuesped_NoExiste() {
        when(huespedRepository.existsById(99)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gestorHuesped.eliminarHuesped(99));
        assertTrue(ex.getMessage().contains("no existe"));
    }
}