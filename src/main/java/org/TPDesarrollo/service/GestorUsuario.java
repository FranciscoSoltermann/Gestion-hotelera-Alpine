package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dto.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;

// Esto es solo un contrato. No lleva @Service.
public interface GestorUsuario {

    Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida;

    Usuario registrarUsuario(UsuarioDTO datos);
}