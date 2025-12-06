package org.TPDesarrollo.service;

import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioExistente;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.mappers.UsuarioMapper;
import org.TPDesarrollo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestorUsuarioImp implements GestorUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public GestorUsuarioImp(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida {
        Usuario usuarioAlmacenado = usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + nombre));

        if (!passwordEncoder.matches(contrasenia, usuarioAlmacenado.getContrasenia())) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }
        return usuarioAlmacenado;
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioDTO datos) {
        if (usuarioRepository.findByNombre(datos.getNombre()).isPresent()) {
            throw new UsuarioExistente("El nombre de usuario '" + datos.getNombre() + "' ya existe.");
        }

        Usuario nuevoUsuario = usuarioMapper.toEntity(datos);

        nuevoUsuario.setContrasenia(passwordEncoder.encode(datos.getContrasenia()));

        return usuarioRepository.save(nuevoUsuario);
    }
}