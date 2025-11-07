package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.Clases.Usuario;
import org.TPDesarrollo.DAOS.UsuarioDAO;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestorUsuario {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Transactional(readOnly = true)
    public boolean autenticarUsuario(String nombre, String contrasenia)
            throws UsuarioNoEncontrado, ContraseniaInvalida {

        Usuario usuarioAlmacenado = usuarioDAO.findByNombre(nombre)
                .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + nombre));

        if (!usuarioAlmacenado.getContrasenia().equals(contrasenia)) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }

        return true;
    }
}
