package org.TPDesarrollo.DAOs;

import org.TPDesarrollo.Clases.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.TPDesarrollo.Enums.TipoDocumento;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HuespedDAO extends JpaRepository<Huesped, Integer> {
    boolean existsByCuit(String cuit);
    boolean existsByDocumento(String documento);

    @Query("SELECT h FROM Huesped h WHERE " +
            "(:apellido IS NULL OR LOWER(h.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) AND " +
            "(:nombre IS NULL OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:tipoDoc IS NULL OR h.tipoDocumento = :tipoDoc) AND " +
            "(:documento IS NULL OR h.documento = :documento)")
    List<Huesped> buscarHuespedesPorCriterios(
            @Param("apellido") String apellido,
            @Param("nombre") String nombre,
            @Param("tipoDoc") TipoDocumento tipoDoc,
            @Param("documento") String documento
    );
}
