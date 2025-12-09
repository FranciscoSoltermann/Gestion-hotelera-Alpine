package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dto.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;

/**
 * Interfaz para la gestión de usuarios.
 */
public interface GestorUsuario {
    /**
     * Autentica a un usuario con el nombre y la contraseña proporcionados.
     *
     * @param nombre     El nombre de usuario.
     * @param contrasenia La contraseña del usuario.
     * @return El usuario autenticado.
     * @throws UsuarioNoEncontrado   Si el usuario no existe.
     * @throws ContraseniaInvalida   Si la contraseña es incorrecta.
     */
    Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida;
    /**
     * Registra un nuevo usuario con los datos proporcionados.
     *
     * @param datos Objeto de transferencia de datos que contiene la información del usuario a registrar.
     * @return El usuario registrado.
     */
    Usuario registrarUsuario(UsuarioDTO datos);
}