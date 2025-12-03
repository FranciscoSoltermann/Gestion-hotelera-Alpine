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

/**
 * Servicio para la gestión de usuarios.
 * Proporciona métodos para autenticar y registrar usuarios.
 * Utiliza UsuarioRepository para interactuar con la base de datos.
 * Incluye manejo de excepciones para casos de usuario no encontrado y contraseña inválida.
 * Incorpora seguridad al encriptar contraseñas utilizando PasswordEncoder.
 * @Service indica que esta clase es un servicio de Spring.
 */
@Service
public class GestorUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // <-- Inyectamos el codificador

    @Autowired
    // Constructor con inyección de dependencias
    public GestorUsuario(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    // Método para autenticar un usuario
    public Usuario autenticarUsuario(String nombre, String contrasenia) throws UsuarioNoEncontrado, ContraseniaInvalida {

        //Buscamos el usuario
        Usuario usuarioAlmacenado = usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new UsuarioNoEncontrado("Usuario no encontrado: " + nombre));

        //Comparamos la contraseña
        if (!passwordEncoder.matches(contrasenia, usuarioAlmacenado.getContrasenia())) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }

        //Devolvemos el usuario encontrado
        return usuarioAlmacenado;
    }

    @Transactional
    public Usuario registrarUsuario(UsuarioDTO datos) {
        //Verifica si el usuario ya existe
        if (usuarioRepository.findByNombre(datos.getNombre()).isPresent()) {
            throw new UsuarioExistente("El nombre de usuario '" + datos.getNombre() + "' ya existe.");
        }

        //Crea el nuevo usuario usando el BUILDER
        Usuario nuevoUsuario = Usuario.builder()
                .nombre(datos.getNombre())
                //Encriptamos la contraseña antes de guardarla
                .contrasenia(passwordEncoder.encode(datos.getContrasenia()))
                .rol("ADMIN")
                .build();

        //Guarda y devuelve el usuario
        return usuarioRepository.save(nuevoUsuario);
    }
}