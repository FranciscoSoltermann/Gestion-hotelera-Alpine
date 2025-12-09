package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repositorio para la entidad Pago.
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    /**
     * Encuentra una lista de pagos asociados a una factura específica.
     *
     * @param factura La factura para la cual se buscan los pagos.
     * @return Una lista de pagos asociados a la factura proporcionada.
     */
    List<Pago> findByFactura(Factura factura);
}