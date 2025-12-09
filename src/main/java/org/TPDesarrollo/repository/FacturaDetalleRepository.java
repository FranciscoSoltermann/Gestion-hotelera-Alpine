package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.FacturaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio para la entidad FacturaDetalle.
 * Proporciona métodos CRUD y de consulta para gestionar los detalles de las facturas en la base de datos.
 */
@Repository
public interface FacturaDetalleRepository extends JpaRepository<FacturaDetalle, Long> {
}