package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.PersonaJuridica;
import org.TPDesarrollo.entity.ResponsableDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio para la entidad ResponsableDePago.
 */
@Repository
public interface ResponsableDePagoRepository extends JpaRepository<ResponsableDePago, Integer> {
    /**
     * Busca una PersonaJuridica por su CUIT.
     *
     * @param cuit El CUIT de la PersonaJuridica a buscar.
     * @return Un Optional que contiene la PersonaJuridica si se encuentra, o vacío si no.
     */
    @Query("SELECT p FROM PersonaJuridica p WHERE p.cuit = :cuit")
    Optional<PersonaJuridica> findByCuit(@Param("cuit") String cuit);
    /**
     * Busca un ResponsableDePago por su documento (CUIT o DNI).
     *
     * @param documento El documento (CUIT o DNI) del ResponsableDePago a buscar.
     * @return Un Optional que contiene el ResponsableDePago si se encuentra, o vacío si no.
     */
    @Query("SELECT r FROM ResponsableDePago r " +
            "WHERE TREAT(r AS PersonaJuridica).cuit = :documento " +
            "OR TREAT(r AS PersonaFisica).cuit = :documento " +
            "OR TREAT(r AS PersonaFisica).dni = :documento")
    Optional<ResponsableDePago> buscarPorDocumento(@Param("documento") String documento);
}