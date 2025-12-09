package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Factura;
import org.TPDesarrollo.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repositorio para la entidad Factura.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas en la base de datos.
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    /**
     * Busca facturas por el número de habitación asociado a la estadía y el estado de la factura.
     *
     * @param numero El número de la habitación.
     * @param estado El estado de la factura.
     * @return Una lista de facturas que coinciden con los criterios especificados.
     */
    @Query("SELECT f FROM Factura f " +
            "JOIN FETCH f.estadia e " +
            "JOIN FETCH e.habitacion h " +
            "JOIN FETCH f.responsableDePago r " +
            "WHERE h.numero = :numero AND f.estado = :estado")
    List<Factura> findByEstadia_Habitacion_NumeroAndEstado(
            @Param("numero") String numero,
            @Param("estado") EstadoFactura estado
    );

    /**
     * Busca facturas activas (no anuladas) por el documento del responsable de pago,
     * que puede ser un DNI para personas físicas o un CUIT para personas jurídicas.
     *
     * @param documento El documento del responsable de pago (DNI o CUIT).
     * @return Una lista de facturas activas asociadas al documento proporcionado.
     */
    @Query("SELECT f FROM Factura f " +
            "JOIN FETCH f.responsableDePago r " +
            "WHERE (TREAT(r AS PersonaFisica).dni = :documento " +  // <-- Usamos TREAT para acceder a propiedades de la subclase
            "    OR TREAT(r AS PersonaJuridica).cuit = :documento) " + // <-- Usamos TREAT aquí también
            "AND f.estado <> 'ANULADA'")
    List<Factura> buscarFacturasActivasPorDocumento(@Param("documento") String documento);

}