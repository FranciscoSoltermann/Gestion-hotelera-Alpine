package org.TPDesarrollo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.exceptions.ContraseniaInvalida;
import org.TPDesarrollo.exceptions.UsuarioExistente;
import org.TPDesarrollo.exceptions.UsuarioNoEncontrado;
import org.TPDesarrollo.mappers.UsuarioMapper;
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
    @Mock private UsuarioMapper usuarioMapper;

    @InjectMocks
    private GestorUsuarioImp gestorUsuario;

    private UsuarioDTO usuarioDTO;
    private Usuario usuarioEntidad;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("admin");
        usuarioDTO.setContrasenia("1234");

        usuarioEntidad = Usuario.builder()
                .nombre("admin")
                .contrasenia("$2a$10$HASHED_PASS")
                .rol("ADMIN")
                .build();

        usuarioEntidad.setId(1L);
    }

    @Test
    void testRegistrarUsuario_CaminoFeliz() {
        // --- ARRANGE ---
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.empty());
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(new Usuario()); // Entidad vacía base
        when(passwordEncoder.encode("1234")).thenReturn("$2a$10$HASHED_PASS");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        // --- ACT ---
        Usuario resultado = gestorUsuario.registrarUsuario(usuarioDTO);

        // --- ASSERT ---
        assertNotNull(resultado);
        verify(passwordEncoder).encode("1234"); // Verifica que se encriptó
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_ErrorUsuarioExistente() {
        // --- ARRANGE ---
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.of(usuarioEntidad));

        // --- ACT & ASSERT ---
        assertThrows(UsuarioExistente.class, () -> gestorUsuario.registrarUsuario(usuarioDTO));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testAutenticarUsuario_Exito() throws Exception {
        // --- ARRANGE ---
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.of(usuarioEntidad));
        // Simulamos que el encoder dice que "1234" coincide con el hash
        when(passwordEncoder.matches("1234", "$2a$10$HASHED_PASS")).thenReturn(true);

        // --- ACT ---
        Usuario resultado = gestorUsuario.autenticarUsuario("admin", "1234");

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals("admin", resultado.getNombre());
    }

    @Test
    void testAutenticarUsuario_ContraseniaIncorrecta() {
        // --- ARRANGE ---
        when(usuarioRepository.findByNombre("admin")).thenReturn(Optional.of(usuarioEntidad));
        when(passwordEncoder.matches("malapass", "$2a$10$HASHED_PASS")).thenReturn(false);

        // --- ACT & ASSERT ---
        assertThrows(ContraseniaInvalida.class, () -> gestorUsuario.autenticarUsuario("admin", "malapass"));
    }

    @Test
    void testAutenticarUsuario_UsuarioNoEncontrado() {
        when(usuarioRepository.findByNombre("fantasma")).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontrado.class, () -> gestorUsuario.autenticarUsuario("fantasma", "123"));
    }
}