package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.PagoDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.enums.EstadoFactura;
import org.TPDesarrollo.enums.RedDePago;
import org.TPDesarrollo.enums.TipoMoneda;
import org.TPDesarrollo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GestorPagoImpTest {

    @Mock private FacturaRepository facturaRepository;
    @Mock private PagoRepository pagoRepository;
    @Mock private MedioDePagoRepository medioDePagoRepository;
    @Mock private BancoRepository bancoRepository;

    @InjectMocks
    private GestorPagoImp gestorPago;

    private Factura factura;
    private PagoDTO pagoDTO;

    @BeforeEach
    void setUp() {
        factura = new Factura();
        factura.setId(1L);
        factura.setMontoTotal(1000f);
        factura.setEstado(EstadoFactura.PENDIENTE);

        pagoDTO = new PagoDTO();
        pagoDTO.setIdFactura(1L);
        pagoDTO.setMonto(1000f);
        pagoDTO.setTipoMedioPago("EFECTIVO");
        pagoDTO.setMoneda(TipoMoneda.PESOS);
    }

    @Test
    void testRegistrarPago_Total_Exito() {
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(pagoRepository.findByFactura(factura)).thenReturn(Collections.emptyList());

        when(medioDePagoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pagoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Pago pagoSimulado = new Pago();
        pagoSimulado.setImporte(1000.0);
        when(pagoRepository.findByFactura(factura)).thenReturn(Collections.singletonList(pagoSimulado));

        // ACT
        Factura resultado = gestorPago.registrarPago(pagoDTO);

        // ASSERT
        assertEquals(EstadoFactura.PAGADA, resultado.getEstado());
        verify(facturaRepository).save(factura);
    }

    @Test
    void testRegistrarPago_Parcial() {
        pagoDTO.setMonto(500f);

        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));

        Pago pagoParcial = new Pago();
        pagoParcial.setImporte(500.0);
        when(pagoRepository.findByFactura(factura)).thenReturn(Collections.singletonList(pagoParcial));

        // ACT
        Factura resultado = gestorPago.registrarPago(pagoDTO);

        // ASSERT
        assertEquals(EstadoFactura.PENDIENTE, resultado.getEstado());
        verify(facturaRepository, never()).save(factura);
    }

    @Test
    void testRegistrarPago_ErrorFacturaPagada() {
        factura.setEstado(EstadoFactura.PAGADA);
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gestorPago.registrarPago(pagoDTO)
        );

        assertEquals("Esta factura ya está pagada.", ex.getMessage());
    }

    @Test
    void testRegistrarPago_ChequeBancoNoExiste() {
        pagoDTO.setTipoMedioPago("CHEQUE");
        pagoDTO.setBanco("Banco Inexistente");
        pagoDTO.setNroCheque("123");

        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(bancoRepository.findByNombre("Banco Inexistente")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gestorPago.registrarPago(pagoDTO));
    }
}