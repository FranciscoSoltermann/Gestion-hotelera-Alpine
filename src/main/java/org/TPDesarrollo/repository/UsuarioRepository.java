package org.TPDesarrollo.repository;

import org.TPDesarrollo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 * Extiende JpaRepository para heredar métodos estándar de acceso a datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreAndContrasenia(String nombre, String contrasenia);
    Optional<Usuario> findByNombre(String nombre);
}
