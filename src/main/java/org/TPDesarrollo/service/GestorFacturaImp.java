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

    @Override
    @Transactional(readOnly = true)
    public ResumenFacturacionDTO buscarEstadiaParaFacturar(String numeroHabitacion, LocalTime horaSalida) {
        // 1. Buscar estadía activa
        Estadia estadia = estadiaRepository.findEstadiaActivaPorHabitacion(numeroHabitacion)
                .orElseThrow(() -> new RuntimeException("No hay estadía activa para la habitación " + numeroHabitacion));

        List<ItemFacturableDTO> items = new ArrayList<>();

        // 2. Determinar precio real según el tipo de habitación
        float precioNoche = obtenerPrecioPorHabitacion(estadia.getHabitacion());

        // 3. Calcular días y recargos
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

        // 4. Cargar consumos [cite: 28]
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

        // 5. Obtener Posibles Responsables de Pago (Ocupantes / Titular de Reserva)
        List<ResponsableDePagoDTO> posiblesResponsables = new ArrayList<>();

        // Buscamos la reserva asociada a la habitación para encontrar al titular
        // Nota: Asumimos que existe un método en ReservaRepository para esto, o filtramos las reservas activas.
        // Si no tienes el método exacto, deberás agregarlo en ReservaRepository.
        reservaRepository.findAll().stream()
                .filter(r -> r.getHabitacion().getIdHabitacion().equals(estadia.getHabitacion().getIdHabitacion())
                        && r.getIngreso().isBefore(LocalDate.now().plusDays(1))
                        && r.getEgreso().isAfter(LocalDate.now().minusDays(1)))
                .findFirst()
                .ifPresent(reserva -> {
                    Integer idTitular = reserva.getHuesped().getId(); // 1. Obtenemos el ID numérico

                    if (idTitular != null) {
                        huespedRepository.findById(idTitular).ifPresent(titular -> {

                            posiblesResponsables.add(ResponsableDePagoDTO.builder()
                                    .id(titular.getId())
                                    .nombreCompleto(titular.getApellido() + " " + titular.getNombre())
                                    .documento(titular.getDocumento())
                                    .tipo("FISICA")
                                    .build());
                        });
                    }
                });

        // 6. Retornar DTO final
        return ResumenFacturacionDTO.builder()
                .idEstadia((long) estadia.getIdestadia())
                .numeroHabitacion(numeroHabitacion)
                .items(items)
                .posiblesResponsables(posiblesResponsables)
                .montoTotal(total)
                .tipoFacturaSugerido(TipoFactura.B) // Por defecto Consumidor Final
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