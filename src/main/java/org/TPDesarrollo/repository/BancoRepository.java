package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio para la entidad Banco.
 */
@Repository
public interface BancoRepository extends JpaRepository<Banco, Integer> {
    /**
     * Busca un banco por su nombre.
     *
     * @param nombre El nombre del banco a buscar.
     * @return Un Optional que contiene el banco si se encuentra, o vacío si no.
     */
    Optional<Banco> findByNombre(String nombre);
}