package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.MedioDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio para la entidad MedioDePago.
 * Proporciona operaciones CRUD y consultas personalizadas para los medios de pago.
 */
@Repository
public interface MedioDePagoRepository extends JpaRepository<MedioDePago, Integer> {

}