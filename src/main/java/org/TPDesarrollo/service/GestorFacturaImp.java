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
import org.hibernate.Hibernate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
/**
 * Implementación del servicio GestorFactura.
 * Proporciona funcionalidades para la gestión de facturación,
 * incluyendo la generación de facturas y notas de crédito.
 */
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
    private final NotaCreditoRepository notaCreditoRepository;
    /**
     * Busca una estadía activa para facturar basada en el número de habitación y la hora de salida.
     *
     * @param numeroHabitacion Número de la habitación.
     * @param horaSalida       Hora de salida para calcular cargos adicionales.
     * @return ResumenFacturacionDTO con los detalles de la facturación.
     */
    @Override
    @Transactional(readOnly = true)
    public ResumenFacturacionDTO buscarEstadiaParaFacturar(String numeroHabitacion, LocalTime horaSalida) {

        Habitacion habitacion = habitacionRepository.findByNumero(numeroHabitacion)
                .orElseThrow(() -> new RuntimeException("Habitación con número " + numeroHabitacion + " no encontrada."));

        List<Estadia> resultados = estadiaRepository.findEstadiaActivaConDetalles(habitacion.getIdHabitacion());

        if (resultados.isEmpty()) {
            throw new RuntimeException("No hay estadía activa...");
        }

        Estadia estadia = resultados.stream()
                .max(Comparator.comparing(Estadia::getFechaHoraIngreso))
                .orElseThrow();

        List<ItemFacturableDTO> items = new ArrayList<>();

        float precioNoche = obtenerPrecioPorHabitacion(estadia.getHabitacion());

        long dias = ChronoUnit.DAYS.between(estadia.getFechaHoraIngreso(), LocalDateTime.of(LocalDate.now(), horaSalida));
        if (dias == 0) dias = 1;

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

        items.addFirst(ItemFacturableDTO.builder()
                .descripcion("Estadía Habitación " + numeroHabitacion + " (" + estadia.getHabitacion().getClass().getSimpleName() + ")")
                .medida("NOCHES")
                .cantidad((int) dias)
                .precioUnitario(precioNoche)
                .subtotal(precioNoche * dias)
                .esEstadia(true)
                .build());

        if (estadia.getItemsConsumo() != null) {
            for (Consumo consumo : estadia.getItemsConsumo()) {

                float subtotalConsumo = consumo.getPrecioUnitario() * consumo.getCantidad();

                items.add(ItemFacturableDTO.builder()
                        .descripcion(consumo.getDescripcion())
                        .medida("UNIDAD")
                        .cantidad(consumo.getCantidad())
                        .precioUnitario(consumo.getPrecioUnitario())
                        .subtotal(subtotalConsumo)
                        .esEstadia(false)
                        .referenciaId((long) consumo.getIdConsumo())
                        .build());
            }
        }

        double total = items.stream().mapToDouble(ItemFacturableDTO::getSubtotal).sum();

        List<ResponsableDePagoDTO> posiblesResponsables = new ArrayList<>();

        if (estadia.getReserva() != null && estadia.getReserva().getHuesped() != null) {
            Huesped titular = estadia.getReserva().getHuesped();

            posiblesResponsables.add(ResponsableDePagoDTO.builder()
                    .id(titular.getId())
                    .nombreCompleto(titular.getApellido() + " " + titular.getNombre())
                    .documento(titular.getDocumento())
                    .tipo("FISICA")
                    .build());
        }

        return ResumenFacturacionDTO.builder()
                .idEstadia((long) estadia.getIdestadia())
                .numeroHabitacion(numeroHabitacion)
                .items(items)
                .posiblesResponsables(posiblesResponsables)
                .montoTotal(total)
                .tipoFacturaSugerido(TipoFactura.B)
                .build();
    }
    /**
     * Genera una factura basada en la solicitud proporcionada.
     *
     * @param solicitud Detalles necesarios para generar la factura.
     * @return Factura generada con todos los detalles asociados.
     */
    @Override
    @Transactional
    public Factura generarFactura(SolicitudFacturaDTO solicitud) {
        // 1. Obtener Estadia
        Estadia estadia = estadiaRepository.findById(solicitud.getIdEstadia().intValue())
                .orElseThrow(() -> new RuntimeException("Estadía no encontrada"));

        ResponsableDePago responsable;

        // 2. Determinar o Crear Responsable de Pago (Lógica de PersonaFisica/Juridica)
        if (solicitud.getCuitTercero() != null && !solicitud.getCuitTercero().isBlank()) {
            String cuit = solicitud.getCuitTercero().trim();
            Optional<ResponsableDePago> existente = responsableRepository.buscarPorDocumento(cuit);

            if (existente.isPresent()) {
                responsable = existente.get();
            } else {
                PersonaJuridica nuevaEmpresa = PersonaJuridica.builder()
                        .razonSocial(solicitud.getRazonSocialTercero())
                        .cuit(cuit)
                        .build();
                responsable = responsableRepository.save(nuevaEmpresa);
            }
        } else {
            Integer idHuesped = solicitud.getIdResponsablePagoSeleccionado();
            Huesped huesped = huespedRepository.findById(idHuesped)
                    .orElseThrow(() -> new RuntimeException("El huésped seleccionado no existe."));

            Optional<ResponsableDePago> responsableExistente = responsableRepository.buscarPorDocumento(huesped.getDocumento());

            if (responsableExistente.isPresent()) {
                responsable = responsableExistente.get();
            } else {
                PersonaFisica nuevoResponsable = PersonaFisica.builder()
                        .nombre(huesped.getNombre())
                        .apellido(huesped.getApellido())
                        .dni(huesped.getDocumento())
                        .direccion(huesped.getDireccion())
                        .build();
                responsable = responsableRepository.save(nuevoResponsable);
            }
        }

        // 3. Construir la Factura inicial
        Factura factura = Factura.builder()
                .estado(EstadoFactura.PENDIENTE)
                .fechaEmision(LocalDate.now())
                .tipoFactura(solicitud.getTipoFactura())
                .responsableDePago(responsable)
                .estadia(estadia)
                .build();

        float totalFactura = 0f;
        float totalEstadia = 0f;
        List<FacturaDetalle> detalles = new ArrayList<>();

        // 4. Crear Detalles y calcular totales
        for (ItemFacturableDTO item : solicitud.getItemsAFacturar()) {
            float subtotalReal = item.getPrecioUnitario() * item.getCantidad();

            FacturaDetalle detalle = FacturaDetalle.builder()
                    .factura(factura)
                    .descripcion(item.getDescripcion())
                    .medida(item.getMedida())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .subtotal(subtotalReal)
                    .esEstadia(item.isEsEstadia()) // <-- AÑADIR ESTA LÍNEA CLAVE
                    .build();

            detalles.add(detalle);
            totalFactura += subtotalReal;

            if (item.isEsEstadia()) {
                totalEstadia += subtotalReal;
            }
        }

        // 5. Asignar Totales a la Factura
        factura.setMontoTotal(totalFactura);
        factura.setValorEstadia(totalEstadia);

        // 6. Guardar Factura Principal
        Factura facturaGuardada = facturaRepository.save(factura);

        // 7. Asociar Factura guardada a Detalles y guardar Detalles
        detalles.forEach(d -> d.setFactura(facturaGuardada));
        facturaDetalleRepository.saveAll(detalles);

        // . CRUCIAL: Actualizar la lista en memoria antes de retornar
        facturaGuardada.setDetalles(detalles);


        // 8. CRUCIAL: Inicializar la colección de detalles (LAZY loading)
        Hibernate.initialize(facturaGuardada.getDetalles());

        // 9. Rellenar campos Transitorios del Responsable de Pago (Para el Frontend)
        if (responsable instanceof PersonaFisica pf) {
            // Asumiendo que Factura.java tiene setResponsableNombre y setResponsableDoc (transitorios)
            facturaGuardada.setResponsableNombre(pf.getApellido() + " " + pf.getNombre());
            facturaGuardada.setResponsableDoc(pf.getDni()); // o CUIT, según la configuración de tu entidad
        } else if (responsable instanceof PersonaJuridica pj) {
            facturaGuardada.setResponsableNombre(pj.getRazonSocial());
            facturaGuardada.setResponsableDoc(pj.getCuit());
        }

        // 10. Actualizar Estadia (Fecha Egreso)
        estadia.setFechaHoraEgreso(LocalDateTime.of(LocalDate.now(), solicitud.getHoraSalida()));
        estadiaRepository.save(estadia);

        // 11. Retornar la Factura completa y serializable
        return facturaGuardada;
    }
    /**
     * Obtiene el precio por noche según el tipo de habitación.
     *
     * @param habitacion La habitación para la cual se desea obtener el precio.
     * @return Precio por noche de la habitación.
     */
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
    /**
     * Busca facturas activas (no anuladas) por el documento del cliente.
     *
     * @param documento Documento del cliente (DNI o CUIT).
     * @return Lista de facturas activas asociadas al cliente.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Factura> buscarFacturasPorCliente(String documento) {
        // Implementa paso 4 del CU: Buscar por DNI/CUIT y que NO estén anuladas
        return facturaRepository.buscarFacturasActivasPorDocumento(documento);
    }
    /**
     * Genera una nota de crédito para anular facturas seleccionadas.
     *
     * @param solicitud Detalles necesarios para generar la nota de crédito.
     * @return Nota de crédito generada con las facturas anuladas.
     */
    @Override
    @Transactional
    public NotaCredito generarNotaCredito(SolicitudNotaCreditoDTO solicitud) {
        // 1. Validar que la lista no venga vacía
        if (solicitud.getIdsFacturas() == null || solicitud.getIdsFacturas().isEmpty()) {
            throw new RuntimeException("No se seleccionaron facturas para anular.");
        }

        // 2. Buscar todas las facturas
        List<Factura> facturas = facturaRepository.findAllById(solicitud.getIdsFacturas());

        if (facturas.isEmpty()) {
            throw new RuntimeException("No se encontraron facturas con los IDs proporcionados.");
        }

        double sumaTotal = 0.0;

        // 3. Procesar cada factura
        for (Factura f : facturas) {
            // Validación: No anular lo ya anulado
            if (f.getEstado() == EstadoFactura.ANULADA) {
                throw new RuntimeException("La factura " + f.getId() + " ya se encuentra ANULADA.");
            }

            // Acumular monto (tu entidad Factura usa float, NotaCredito usa Double)
            sumaTotal += f.getMontoTotal();

            // Cambio de estado (Paso 9 CU)
            f.setEstado(EstadoFactura.ANULADA);

            // NOTA: Al pasar a ANULADA, el método 'buscarEstadiaParaFacturar'
            // volverá a encontrar la estadía disponible para facturar de nuevo,
            // cumpliendo con "dejar la deuda asociada como pendiente".
        }

        // 4. Crear Nota de Crédito
        NotaCredito nc = NotaCredito.builder()
                .fechaEmision(LocalDate.now())
                .montoTotal(sumaTotal)
                .facturasCanceladas(facturas) // Relacionamos las facturas
                .build();

        // 5. Guardar (Cascade actualizará las facturas también)
        return notaCreditoRepository.save(nc);
    }


}