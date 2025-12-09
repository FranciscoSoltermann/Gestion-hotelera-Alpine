package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.MedioDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedioDePagoRepository extends JpaRepository<MedioDePago, Integer> {

}