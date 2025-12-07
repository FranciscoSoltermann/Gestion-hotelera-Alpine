package org.TPDesarrollo.service;

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.dto.CargaConsumoDTO;
import org.TPDesarrollo.entity.Consumo;
import org.TPDesarrollo.entity.Estadia;
import org.TPDesarrollo.entity.Habitacion;
import org.TPDesarrollo.enums.EstadoHabitacion;
import org.TPDesarrollo.repository.ConsumoRepository;
import org.TPDesarrollo.repository.EstadiaRepository;
import org.TPDesarrollo.repository.HabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GestorConsumoImp implements GestorConsumo {

    private final EstadiaRepository estadiaRepository;
    private final ConsumoRepository consumoRepository;
    private final HabitacionRepository habitacionRepository; // <--- Nuevo inyectado

    @Override
    @Transactional
    public void cargarConsumo(CargaConsumoDTO dto) {

        // 1. Buscar la habitación por número (para validar su estado primero)
        Habitacion habitacion = habitacionRepository.findByNumero(dto.getNumeroHabitacion())
                .orElseThrow(() -> new RuntimeException("La habitación " + dto.getNumeroHabitacion() + " no existe."));

        // 3. Buscar la estadía activa usando el ID de la habitación validada
        // Usamos el método estándar que ya tenías en tu repositorio
        Estadia estadia = estadiaRepository.findByHabitacionIdHabitacionAndFechaHoraEgresoIsNull(habitacion.getIdHabitacion())
                .orElseThrow(() -> new RuntimeException("Error de consistencia: La habitación figura OCUPADA pero no tiene estadía activa."));

        // 4. Crear el consumo
        Consumo consumo = Consumo.builder()
                .descripcion(dto.getDescripcion())
                .precioUnitario(dto.getPrecioUnitario())
                .cantidad(dto.getCantidad())
                .fechaConsumo(LocalDateTime.now())
                .estadia(estadia)
                .build();

        // 5. Guardar
        consumoRepository.save(consumo);
    }
}