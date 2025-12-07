package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.TPDesarrollo.enums.TipoDocumento;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repositorio para la entidad Huesped.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas en la base de datos.
 * Extiende JpaRepository para heredar métodos estándar de acceso a datos.
 * Incluye consultas para verificar la existencia de huéspedes por CUIT y documento,
 * así como para buscar huéspedes según varios criterios.
 */
public interface HuespedRepository extends JpaRepository<Huesped, Integer> {
    boolean existsByCuit(String cuit);
    boolean existsByDocumento(String documento);
    Huesped findByDocumento(String documento);

    /**
     * Buscar huéspedes según varios criterios opcionales.
     */
    @Query("SELECT h FROM Huesped h WHERE " +
            "(:apellido IS NULL OR :apellido = '' OR LOWER(h.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) AND " +
            "(:nombre IS NULL OR :nombre = '' OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:tipoDoc IS NULL OR h.tipoDocumento = :tipoDoc) AND " +
            "(:documento IS NULL OR :documento = '' OR h.documento = :documento)")
    /**
     * Buscar huéspedes por criterios opcionales.
     */
    List<Huesped> buscarHuespedesPorCriterios(
            @Param("apellido") String apellido,
            @Param("nombre") String nombre,
            @Param("tipoDoc") TipoDocumento tipoDoc,
            @Param("documento") String documento
    );
}
