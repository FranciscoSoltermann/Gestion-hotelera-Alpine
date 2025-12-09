package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Integer> {
}