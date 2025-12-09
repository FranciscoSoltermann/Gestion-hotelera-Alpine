package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.ItemFacturableDTO;
import org.TPDesarrollo.dto.ResumenFacturacionDTO;
import org.TPDesarrollo.dto.SolicitudFacturaDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.entity.habitaciones.IndividualEstandar;
import org.TPDesarrollo.enums.EstadoFactura;
import org.TPDesarrollo.enums.TipoFactura;
import org.TPDesarrollo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GestorFacturaImpTest {

    @Mock private EstadiaRepository estadiaRepository;
    @Mock private FacturaRepository facturaRepository;
    @Mock private FacturaDetalleRepository facturaDetalleRepository;
    @Mock private ResponsableDePagoRepository responsableRepository;
    @Mock private HuespedRepository huespedRepository;
    @Mock private HabitacionRepository habitacionRepository;

    @InjectMocks
    private GestorFacturaImp gestorFactura;

    private Estadia estadia;
    private Habitacion habitacion;
    private Huesped huesped;

    @BeforeEach
    void setUp() {
        habitacion = new IndividualEstandar();
        habitacion.setIdHabitacion(1);
        habitacion.setNumero(String.valueOf(101));

        huesped = new Huesped();
        huesped.setId(1);
        huesped.setNombre("Juan");
        huesped.setApellido("Perez");
        huesped.setDocumento("12345678");

        Reserva reserva = new Reserva();
        reserva.setHuesped(huesped);

        estadia = new Estadia();
        estadia.setIdestadia(10);
        estadia.setHabitacion(habitacion);
        estadia.setReserva(reserva);
        estadia.setFechaHoraIngreso(LocalDateTime.now().minusDays(1));
        estadia.setItemsConsumo(new ArrayList<>());
    }

    /**
     * Test Búsqueda de Estadía para Facturar.
     * Verifica que calcule bien los items (noches + consumos).
     */
    @Test
    void testBuscarEstadiaParaFacturar_Exito() {
        when(habitacionRepository.findByNumero("101")).thenReturn(Optional.of(habitacion));
        when(estadiaRepository.findEstadiaActivaConDetalles(1))
                .thenReturn(Collections.singletonList(estadia));

        Consumo consumo = new Consumo();
        consumo.setDescripcion("Agua");
        consumo.setPrecioUnitario(100f);
        consumo.setCantidad(2);
        estadia.getItemsConsumo().add(consumo);

        // ACT
        ResumenFacturacionDTO resumen = gestorFactura.buscarEstadiaParaFacturar("101", LocalTime.of(10, 0));

        // ASSERT
        assertNotNull(resumen);
        assertEquals(2, resumen.getItems().size());
        assertEquals(10L, resumen.getIdEstadia());

        assertEquals(51000.0, resumen.getMontoTotal(), 0.1);
    }

    /**
     * Test Late Check-out.
     * Si sale después de las 11:00 y antes de las 18:00, cobra medio día.
     */
    @Test
    void testBuscarEstadiaParaFacturar_LateCheckout() {
        when(habitacionRepository.findByNumero("101")).thenReturn(Optional.of(habitacion));
        when(estadiaRepository.findEstadiaActivaConDetalles(1)).thenReturn(Collections.singletonList(estadia));

        ResumenFacturacionDTO resumen = gestorFactura.buscarEstadiaParaFacturar("101", LocalTime.of(15, 0));

        assertTrue(resumen.getItems().stream().anyMatch(i -> i.getDescripcion().contains("Late Check-out")));
    }

    /**
     * Test Generar Factura (Finalizar Estadía).
     */
    @Test
    void testGenerarFactura_Exito() {
        SolicitudFacturaDTO solicitud = new SolicitudFacturaDTO();
        solicitud.setIdEstadia(10L);
        solicitud.setIdResponsablePagoSeleccionado(1);
        solicitud.setTipoFactura(TipoFactura.B);
        solicitud.setHoraSalida(LocalTime.of(10, 0));

        ItemFacturableDTO item1 = ItemFacturableDTO.builder()
                .descripcion("Noche").cantidad(1).precioUnitario(50000f).esEstadia(true).build();
        solicitud.setItemsAFacturar(Collections.singletonList(item1));

        when(estadiaRepository.findById(10)).thenReturn(Optional.of(estadia));
        when(huespedRepository.findById(1)).thenReturn(Optional.of(huesped));

        PersonaFisica responsable = new PersonaFisica();
        when(responsableRepository.buscarPorDocumento(huesped.getDocumento())).thenReturn(Optional.of(responsable));

        when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> {
            Factura f = i.getArgument(0);
            f.setDetalles(new ArrayList<>());
            return f;
        });

        // ACT
        Factura facturaGenerada = gestorFactura.generarFactura(solicitud);

        // ASSERT
        assertNotNull(facturaGenerada);
        assertEquals(EstadoFactura.PENDIENTE, facturaGenerada.getEstado());
        assertEquals(50000f, facturaGenerada.getMontoTotal());

        verify(estadiaRepository).save(argThat(e -> e.getFechaHoraEgreso() != null));
        verify(facturaDetalleRepository).saveAll(anyList());
    }
}