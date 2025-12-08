package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByEstadia_Habitacion_NumeroAndEstado(String numeroHabitacion, EstadoFactura estado);
}