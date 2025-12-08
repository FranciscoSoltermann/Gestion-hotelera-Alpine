package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f " +
            "JOIN FETCH f.estadia e " +
            "JOIN FETCH e.habitacion h " +
            "JOIN FETCH f.responsableDePago r " +
            "WHERE h.numero = :numero AND f.estado = :estado")
    List<Factura> findByEstadia_Habitacion_NumeroAndEstado(
            @Param("numero") String numero,
            @Param("estado") EstadoFactura estado
    );
}