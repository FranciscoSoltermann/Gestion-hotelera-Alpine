package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.ResponsableDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsableDePagoRepository extends JpaRepository<ResponsableDePago, Integer> {
    // Aquí puedes agregar métodos personalizados si necesitas buscar por CUIT o DNI en el futuro
}