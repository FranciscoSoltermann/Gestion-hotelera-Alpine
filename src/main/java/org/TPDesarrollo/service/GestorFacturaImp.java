package org.TPDesarrollo.service;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.*;
import org.TPDesarrollo.entity.*;
import org.TPDesarrollo.entity.habitaciones.*;
import org.TPDesarrollo.enums.EstadoFactura;
import org.TPDesarrollo.enums.TipoFactura;
import org.TPDesarrollo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GestorFacturaImp implements GestorFactura {

    private final EstadiaRepository estadiaRepository;
    private final FacturaRepository facturaRepository;
    private final FacturaDetalleRepository facturaDetalleRepository;
    private final ResponsableDePagoRepository responsableRepository;
    private final ReservaRepository reservaRepository;
    private final HuespedRepository huespedRepository;
    private final HabitacionRepository habitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public ResumenFacturacionDTO buscarEstadiaParaFacturar(String numeroHabitacion, LocalTime horaSalida) {

        // 1. BÚSQUEDA EN DOS PASOS: Número (String) -> ID (Integer)
        Habitacion habitacion = habitacionRepository.findByNumero(numeroHabitacion)
                .orElseThrow(() -> new RuntimeException("Habitación con número " + numeroHabitacion + " no encontrada."));

        // 2. Buscar estadía activa por el ID interno de la Habitacion.
        Estadia estadia = estadiaRepository.findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(habitacion.getIdHabitacion())
                .orElseThrow(() -> new RuntimeException("No hay estadía activa para la habitación " + numeroHabitacion));

        List<ItemFacturableDTO> items = new ArrayList<>();

        // 3. Determinar precio real según el tipo de habitación
        float precioNoche = obtenerPrecioPorHabitacion(estadia.getHabitacion());

        // 4. Calcular días y recargos
        long dias = ChronoUnit.DAYS.between(estadia.getFechaHoraIngreso(), LocalDateTime.of(LocalDate.now(), horaSalida));
        if (dias == 0) dias = 1; // Cobrar al menos una noche

        if (horaSalida.isAfter(LocalTime.of(11, 0))) {
            if (horaSalida.isBefore(LocalTime.of(18, 0))) {
                // Agregar ítem de Late Check-out (50%)
                items.add(ItemFacturableDTO.builder()
                        .descripcion("Late Check-out (50%)")
                        .cantidad(1)
                        .precioUnitario(precioNoche * 0.5f)
                        .subtotal(precioNoche * 0.5f)
                        .esEstadia(true)
                        .build());
            } else {
                dias++;
            }
        }

        // Agregar el costo base de la estadía
        items.add(0, ItemFacturableDTO.builder()
                .descripcion("Estadía Habitación " + numeroHabitacion + " (" + estadia.getHabitacion().getClass().getSimpleName() + ")")
                .cantidad((int) dias)
                .precioUnitario(precioNoche)
                .subtotal(precioNoche * dias)
                .esEstadia(true)
                .build());

        // 5. Cargar consumos
        if (estadia.getItemsConsumo() != null) {
            for (Consumo consumo : estadia.getItemsConsumo()) {
                items.add(ItemFacturableDTO.builder()
                        .descripcion(consumo.getDescripcion())
                        .cantidad(1)
                        .precioUnitario(consumo.getPrecio())
                        .subtotal(consumo.getPrecio())
                        .esEstadia(false)
                        .referenciaId((long) consumo.getIdconsumo())
                        .build());
            }
        }

        double total = items.stream().mapToDouble(ItemFacturableDTO::getSubtotal).sum();

        // 6. Obtener Posibles Responsables de Pago (SIMPLIFICADO)
        List<ResponsableDePagoDTO> posiblesResponsables = new ArrayList<>();

        // Usamos la Reserva referenciada directamente por la Estadia (que fue el objeto de la corrección inicial)
        if (estadia.getReserva() != null && estadia.getReserva().getHuesped() != null) {
            Huesped titular = estadia.getReserva().getHuesped();

            posiblesResponsables.add(ResponsableDePagoDTO.builder()
                    .id(titular.getId())
                    .nombreCompleto(titular.getApellido() + " " + titular.getNombre())
                    .documento(titular.getDocumento())
                    .tipo("FISICA")
                    .build());
        }

        // 7. Retornar DTO final
        return ResumenFacturacionDTO.builder()
                .idEstadia((long) estadia.getIdestadia())
                .numeroHabitacion(numeroHabitacion)
                .items(items)
                .posiblesResponsables(posiblesResponsables)
                .montoTotal(total)
                .tipoFacturaSugerido(TipoFactura.B)
                .build();
    }

    @Override
    @Transactional
    public Factura generarFactura(SolicitudFacturaDTO solicitud) {
        // 1. Obtener Entidades
        Estadia estadia = estadiaRepository.findById(solicitud.getIdEstadia().intValue())
                .orElseThrow(() -> new RuntimeException("Estadía no encontrada"));

        ResponsableDePago responsable = responsableRepository.findById(solicitud.getIdResponsablePagoSeleccionado())
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        // 2. Crear cabecera de Factura
        Factura factura = Factura.builder()
                .estado(EstadoFactura.PENDIENTE)
                .fechaEmision(LocalDate.now())
                .tipoFactura(solicitud.getTipoFactura())
                .responsableDePago(responsable)
                .estadia(estadia)
                .build();

        // 3. Procesar items y calcular totales
        float totalFactura = 0f;
        float totalEstadia = 0f;
        List<FacturaDetalle> detalles = new ArrayList<>();

        for (ItemFacturableDTO item : solicitud.getItemsAFacturar()) {
            FacturaDetalle detalle = FacturaDetalle.builder()
                    .factura(factura)
                    .descripcion(item.getDescripcion())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .subtotal(item.getSubtotal())
                    .build();

            detalles.add(detalle);
            totalFactura += item.getSubtotal();

            if (item.isEsEstadia()) {
                totalEstadia += item.getSubtotal();
            }
        }

        factura.setMontoTotal(totalFactura);
        factura.setValorEstadia(totalEstadia);

        // 4. Persistir Factura y Detalles
        Factura facturaGuardada = facturaRepository.save(factura);

        detalles.forEach(d -> d.setFactura(facturaGuardada));
        facturaDetalleRepository.saveAll(detalles);

        // 5. Actualizar Estadía (Egreso Real)
        estadia.setFechaHoraEgreso(LocalDateTime.of(LocalDate.now(), solicitud.getHoraSalida()));
        estadiaRepository.save(estadia);

        return facturaGuardada;
    }

    private float obtenerPrecioPorHabitacion(Habitacion habitacion) {
        if (habitacion instanceof IndividualEstandar) {
            return 50_800.0f;
        } else if (habitacion instanceof DobleEstandar) {
            return 70_230.0f;
        } else if (habitacion instanceof DobleSuperior) {
            return 90_560.0f;
        } else if (habitacion instanceof SuperiorFamilyPlan) {
            return 110_500.0f;
        } else if (habitacion instanceof SuiteDoble) {
            return 128_600.0f;
        }
        return 0.0f;
    }
}