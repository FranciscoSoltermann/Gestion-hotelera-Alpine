package org.TPDesarrollo.service;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.CargaConsumoDTO;
import org.TPDesarrollo.entity.Consumo;
import org.TPDesarrollo.entity.Estadia;
import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.repository.ConsumoRepository;
import org.TPDesarrollo.repository.EstadiaRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
/**
 * Implementación del servicio GestorConsumo.
 * Proporciona la funcionalidad para cargar consumos asociados a estadías de habitaciones.
 */
@Service
@RequiredArgsConstructor
public class GestorConsumoImp implements GestorConsumo {

    private final EstadiaRepository estadiaRepository;
    private final ConsumoRepository consumoRepository;
    private final HabitacionRepository habitacionRepository;
    /**
     * Carga un consumo asociado a la estadía activa de una habitación.
     *
     * @param dto El DTO que contiene los datos del consumo a cargar.
     * @throws RuntimeException Si la habitación no existe o no tiene una estadía activa.
     */
    @Override
    @Transactional
    public void cargarConsumo(CargaConsumoDTO dto) {

        Habitacion habitacion = habitacionRepository.findByNumero(dto.getNumeroHabitacion())
                .orElseThrow(() -> new RuntimeException("La habitación " + dto.getNumeroHabitacion() + " no existe."));

        List<Estadia> estadiasActivas = estadiaRepository.findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(habitacion.getIdHabitacion());

        if (estadiasActivas.isEmpty()) {
            throw new RuntimeException("Error: La habitación figura OCUPADA pero no tiene estadía activa en el sistema.");
        }

        Estadia estadia = estadiasActivas.stream()
                .max(Comparator.comparing(Estadia::getFechaHoraIngreso))
                .orElseThrow();

        Consumo consumo = Consumo.builder()
                .descripcion(dto.getDescripcion())
                .precioUnitario(dto.getPrecioUnitario())
                .cantidad(dto.getCantidad())
                .fechaConsumo(LocalDateTime.now())
                .estadia(estadia)
                .build();

        consumoRepository.save(consumo);
    }
}