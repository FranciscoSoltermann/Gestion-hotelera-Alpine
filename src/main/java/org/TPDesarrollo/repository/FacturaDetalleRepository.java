package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.FacturaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaDetalleRepository extends JpaRepository<FacturaDetalle, Long> {
}