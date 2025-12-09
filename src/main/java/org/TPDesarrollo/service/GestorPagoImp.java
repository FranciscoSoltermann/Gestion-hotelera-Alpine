package org.TPDesarrollo.service;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.PagoDTO;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.enums.EstadoFactura;
import org.TPDesarrollo.enums.RedDePago;
import org.TPDesarrollo.enums.TipoMoneda;
import org.TPDesarrollo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
/**
 * Implementación del gestor de pagos.
 * Proporciona funcionalidades para registrar pagos y obtener facturas pendientes con saldo.
 */
@Service
@RequiredArgsConstructor
public class GestorPagoImp implements GestorPago{

    private final FacturaRepository facturaRepository;
    private final PagoRepository pagoRepository;
    private final MedioDePagoRepository medioDePagoRepository;
    private final BancoRepository bancoRepository;
    /**
     * Registra un pago para una factura específica.
     *
     * @param dto El DTO que contiene la información del pago.
     * @return La factura actualizada después del registro del pago.
     */
    @Transactional
    public Factura registrarPago(PagoDTO dto) {
        Factura factura = facturaRepository.findById((long) Math.toIntExact(dto.getIdFactura()))
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new RuntimeException("Esta factura ya está pagada.");
        }

        MedioDePago medio = crearMedioDePago(dto);
        medioDePagoRepository.save(medio);

        Pago pago = Pago.builder()
                .fechaCobro(LocalDate.now())
                .importe((double) dto.getMonto())
                .cotizacion((double) (dto.getCotizacion() != null ? dto.getCotizacion() : 1.0f))
                .moneda(dto.getMoneda() != null ? dto.getMoneda() : TipoMoneda.PESOS)
                .factura(factura)
                .medioDePago(medio)
                .build();

        pagoRepository.save(pago);

        validarEstadoFactura(factura);

        return factura;
    }
    /**
     * Obtiene una lista de facturas pendientes con saldo para una habitación específica.
     *
     * @param habitacion El número de la habitación.
     * @return Una lista de facturas pendientes con su saldo pendiente calculado.
     */
    @Transactional(readOnly = true)
    public List<Factura> obtenerPendientesConSaldo(String habitacion) {
        List<Factura> pendientes = facturaRepository.findByEstadia_Habitacion_NumeroAndEstado(
                habitacion,
                EstadoFactura.PENDIENTE
        );

        for (Factura f : pendientes) {

            List<Pago> pagos = pagoRepository.findByFactura(f);
            double pagado = pagos.stream().mapToDouble(Pago::getImporte).sum();
            f.setSaldoPendiente(f.getMontoTotal() - pagado);
        }

        return pendientes;
    }
    /**
     * Crea un medio de pago basado en la información proporcionada en el DTO.
     *
     * @param dto El DTO que contiene la información del medio de pago.
     * @return El medio de pago creado.
     */
    private MedioDePago crearMedioDePago(PagoDTO dto) {
        return switch (dto.getTipoMedioPago().toUpperCase()) {
            case "EFECTIVO" -> Efectivo.builder()
                    .build();
            case "TARJETA_CREDITO" -> Credito.builder()
                    .numeroTarjeta(Integer.parseInt(dto.getNumeroTarjeta()))
                    .cuotas(1)
                    .redDePago(RedDePago.valueOf(dto.getNombreTarjeta().toUpperCase()))
                    .build();
            case "TARJETA_DEBITO" -> Debito.builder()
                    .numeroTarjeta(Integer.parseInt(dto.getNumeroTarjeta()))
                    .redDePago(RedDePago.valueOf(dto.getNombreTarjeta().toUpperCase()))
                    .build();
            case "CHEQUE" -> {
                Banco banco = bancoRepository.findByNombre(dto.getBanco())
                        .orElseThrow(() -> new RuntimeException("El banco '" + dto.getBanco() + "' no existe."));

                yield Cheque.builder()
                        .numeroCheque(Integer.parseInt(dto.getNroCheque()))
                        .banco(banco)
                        .Plaza(dto.getPlaza())
                        .fechaCheque(dto.getFechaCobro())
                        .build();
            }
            default -> throw new RuntimeException("Medio de pago no soportado: " + dto.getTipoMedioPago());
        };
    }
    /**
     * Valida y actualiza el estado de una factura si el total pagado cubre el monto total.
     *
     * @param factura La factura a validar.
     */
    private void validarEstadoFactura(Factura factura) {
        List<Pago> pagos = pagoRepository.findByFactura(factura);

        double totalPagado = pagos.stream().mapToDouble(Pago::getImporte).sum();

        if (totalPagado >= (factura.getMontoTotal() - 0.01)) {
            factura.setEstado(EstadoFactura.PAGADA);
            facturaRepository.save(factura);
        }
    }
}