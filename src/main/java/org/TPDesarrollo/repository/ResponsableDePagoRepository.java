package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.PersonaJuridica;
import org.TPDesarrollo.entity.ResponsableDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsableDePagoRepository extends JpaRepository<ResponsableDePago, Integer> {

    @Query("SELECT p FROM PersonaJuridica p WHERE p.cuit = :cuit")
    Optional<PersonaJuridica> findByCuit(@Param("cuit") String cuit);

    @Query("SELECT r FROM ResponsableDePago r " +
            "WHERE TREAT(r AS PersonaJuridica).cuit = :documento " +
            "OR TREAT(r AS PersonaFisica).cuit = :documento " +
            "OR TREAT(r AS PersonaFisica).dni = :documento")
    Optional<ResponsableDePago> buscarPorDocumento(@Param("documento") String documento);
}