package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.repository.UsuarioRepository;
import org.TPDesarrollo.exceptions.UsuarioExistente;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GestorUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // <-- Inyectamos el codificador

    @Autowired
    public GestorUsuario(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    // 1. CAMBIO: De 'void' a 'Usuario'
    public Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida {

        // 1. Buscamos el usuario
        Usuario usuarioAlmacenado = usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + nombre));

        // 2. ¡SEGURIDAD! Comparamos la contraseña
        if (!passwordEncoder.matches(contrasenia, usuarioAlmacenado.getContrasenia())) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }

        // 3. CAMBIO: ¡Devolvemos el usuario encontrado!
        return usuarioAlmacenado;
    }

    @Transactional
    public Usuario registrarUsuario(UsuarioDTO datos) {
        // 1. Verifica si el usuario ya existe
        if (usuarioRepository.findByNombre(datos.getNombre()).isPresent()) {
            throw new UsuarioExistente("El nombre de usuario '" + datos.getNombre() + "' ya existe.");
        }

        // 2. Crea el nuevo usuario usando el BUILDER (¡Mucho más limpio!)
        Usuario nuevoUsuario = Usuario.builder()
                .nombre(datos.getNombre())
                // ¡SEGURIDAD! Encriptamos la contraseña antes de guardarla
                .contrasenia(passwordEncoder.encode(datos.getContrasenia()))
                .rol("ADMIN") // O "USER", según tu lógica
                .build();

        // 3. Guarda y devuelve el usuario
        return usuarioRepository.save(nuevoUsuario);
    }
}