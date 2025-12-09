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
    /**
     * Busca un usuario por su nombre y contraseña.
     *
     * @param nombre      El nombre del usuario.
     * @param contrasenia La contraseña del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findByNombreAndContrasenia(String nombre, String contrasenia);
    /**
     * Busca un usuario por su nombre.
     *
     * @param nombre El nombre del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<Usuario> findByNombre(String nombre);
}
