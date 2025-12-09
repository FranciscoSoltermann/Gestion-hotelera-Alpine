package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio para la entidad NotaCredito.
 */
@Repository
public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Integer> {
}