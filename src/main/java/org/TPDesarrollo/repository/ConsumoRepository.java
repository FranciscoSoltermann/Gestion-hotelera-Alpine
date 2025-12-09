package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Consumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio para la entidad Consumo.
 * Proporciona métodos CRUD y de consulta para gestionar los registros de consumo en la base de datos.
 */
@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Integer> {

}