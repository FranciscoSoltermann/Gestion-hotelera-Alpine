package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.Clases.Usuario;
import org.TPDesarrollo.DTOs.UsuarioDTO;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;
import org.TPDesarrollo.Excepciones.UsuarioExistente;
import org.TPDesarrollo.Repository.UsuarioRepository;
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

        // When
        Usuario resultado = gestorUsuario.registrarUsuario(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DeberiaLanzarExcepcion_CuandoUsuarioYaExiste() {
        // Given
        UsuarioDTO dto = new UsuarioDTO("fran", "12345");
        when(usuarioRepository.findByNombre("fran")).thenReturn(Optional.of(new Usuario()));

        // When & Then
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

        // When & Then
        assertThrows(ContraseniaInvalida.class, () -> gestorUsuario.autenticarUsuario("fran", "1234"));
    }
}