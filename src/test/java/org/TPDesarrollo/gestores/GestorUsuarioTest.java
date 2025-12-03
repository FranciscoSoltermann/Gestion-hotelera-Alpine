package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioExistente;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorUsuarioTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private GestorUsuario gestorUsuario;

    @Test
    void registrarUsuario_DeberiaGuardar_CuandoNoExiste() {
        // Given
        UsuarioDTO dto = new UsuarioDTO("fran", "12345");
        when(usuarioRepository.findByNombre("fran")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("12345")).thenReturn("hash_secreto");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre("fran");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);


        Usuario resultado = gestorUsuario.registrarUsuario(dto);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DeberiaLanzarExcepcion_CuandoUsuarioYaExiste() {
        // Given
        UsuarioDTO dto = new UsuarioDTO("fran", "12345");
        when(usuarioRepository.findByNombre("fran")).thenReturn(Optional.of(new Usuario()));


        assertThrows(UsuarioExistente.class, () -> gestorUsuario.registrarUsuario(dto));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void autenticarUsuario_DeberiaLanzarExcepcion_SiPasswordEsIncorrecta() {
        // Given
        Usuario usuarioEnBd = new Usuario();
        usuarioEnBd.setContrasenia("hash_real");

        when(usuarioRepository.findByNombre("fran")).thenReturn(Optional.of(usuarioEnBd));
        when(passwordEncoder.matches("1234", "hash_real")).thenReturn(false);


        assertThrows(ContraseniaInvalida.class, () -> gestorUsuario.autenticarUsuario("fran", "1234"));
    }
    @Test
    void autenticarUsuario_DeberiaRetornarUsuario_SiCredencialesSonCorrectas() throws UsuarioNoEncontrado, ContraseniaInvalida {
        // Given
        String nombre = "fran";
        String passIngresada = "12345";
        String hashEnBd = "hash_valido";

        Usuario usuarioEnBd = new Usuario();
        usuarioEnBd.setNombre(nombre);
        usuarioEnBd.setContrasenia(hashEnBd);

        // Simulamos que el usuario existe
        when(usuarioRepository.findByNombre(nombre)).thenReturn(Optional.of(usuarioEnBd));
        // Simulamos que la contraseña coincide
        when(passwordEncoder.matches(passIngresada, hashEnBd)).thenReturn(true);

        // When
        Usuario resultado = gestorUsuario.autenticarUsuario(nombre, passIngresada);

        // Then
        assertNotNull(resultado);
        assertEquals(nombre, resultado.getNombre());
    }

    @Test
    void autenticarUsuario_DeberiaLanzarExcepcion_SiUsuarioNoExiste() {
        // Given
        String nombre = "fantasma";
        when(usuarioRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        // When & Then

        assertThrows(org.TPDesarrollo.exceptions.UsuarioNoEncontrado.class, () -> {
            gestorUsuario.autenticarUsuario(nombre, "cualquier_pass");
        });
    }
}