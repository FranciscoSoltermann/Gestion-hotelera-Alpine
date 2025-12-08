package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.dto.UsuarioDTO;
import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioExistente;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GestorUsuarioImpTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private GestorUsuarioImp gestorUsuario;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("admin")
                .contrasenia("encodedPassword")
                .rol("ADMIN")
                .build();

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("admin");
        usuarioDTO.setContrasenia("1234");
    }

    @Test
    void testAutenticarUsuario_Exito() throws UsuarioNoEncontrado, ContraseniaInvalida {
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("1234", "encodedPassword")).thenReturn(true);

        Usuario resultado = gestorUsuario.autenticarUsuario("admin", "1234");

        assertNotNull(resultado);
        assertEquals("admin", resultado.getNombre());
    }

    @Test
    void testAutenticarUsuario_NoEncontrado() {
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontrado.class, () ->
                gestorUsuario.autenticarUsuario("admin", "1234")
        );
    }

    @Test
    void testAutenticarUsuario_ContraseniaIncorrecta() {
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        assertThrows(ContraseniaInvalida.class, () ->
                gestorUsuario.autenticarUsuario("admin", "wrong")
        );
    }

    @Test
    void testRegistrarUsuario_Exito() {
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.empty()); // No existe
        when(passwordEncoder.encode("1234")).thenReturn("encodedNew");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = gestorUsuario.registrarUsuario(usuarioDTO);

        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_YaExiste() {
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.of(usuario));

        assertThrows(UsuarioExistente.class, () ->
                gestorUsuario.registrarUsuario(usuarioDTO)
        );

        verify(usuarioRepository, never()).save(any());
    }
}