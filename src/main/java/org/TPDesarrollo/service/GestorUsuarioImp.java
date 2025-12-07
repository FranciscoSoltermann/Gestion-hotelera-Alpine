package org.TPDesarrollo.service; // Asegurate que el paquete coincida

import lombok.RequiredArgsConstructor;
import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dto.UsuarioDTO;
import org.TPDesarrollo.repository.UsuarioRepository;
import org.TPDesarrollo.exceptions.UsuarioExistente;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la interfaz GestorUsuario.
 * Contiene la lógica real.
 */
@Service // ¡IMPORTANTE! La anotación @Service va en la implementación
@RequiredArgsConstructor // Usamos Lombok para inyectar dependencias (más limpio)
public class GestorUsuarioImp implements GestorUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override // Indica que estamos cumpliendo el contrato de la interfaz
    @Transactional(readOnly = true)
    public Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida {

        // Buscamos el usuario
        Usuario usuarioAlmacenado = usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + nombre));

        // Comparamos la contraseña encriptada
        if (!passwordEncoder.matches(contrasenia, usuarioAlmacenado.getContrasenia())) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }

        return usuarioAlmacenado;
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioDTO datos) {
        // Verifica si ya existe
        if (usuarioRepository.findByNombre(datos.getNombre()).isPresent()) {
            throw new UsuarioExistente("El nombre de usuario '" + datos.getNombre() + "' ya existe.");
        }

        // Crea el usuario con el patrón Builder
        Usuario nuevoUsuario = Usuario.builder()
                .nombre(datos.getNombre())
                .contrasenia(passwordEncoder.encode(datos.getContrasenia())) // Encriptar siempre
                .rol("ADMIN") // O lo que corresponda
                .build();

        return usuarioRepository.save(nuevoUsuario);
    }
}