package org.TPDesarrollo.service;

import org.TPDesarrollo.clases.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;

public interface GestorUsuario {

    Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida;

    Usuario registrarUsuario(UsuarioDTO datos);
}