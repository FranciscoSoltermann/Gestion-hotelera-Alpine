package org.TPDesarrollo.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.TPDesarrollo.clases.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.TPDesarrollo.gestores.GestorUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioControlador.class)
@AutoConfigureMockMvc(addFilters = false) // Desactivamos seguridad para facilitar el test unitario
class UsuarioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GestorUsuario gestorUsuario;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_DeberiaDevolver200_Y_Usuario() throws Exception {
        UsuarioDTO loginDto = new UsuarioDTO("fran", "12345AAA"); // Pass cumple reglas
        Usuario usuarioRetornado = new Usuario();
        usuarioRetornado.setNombre("fran");

        when(gestorUsuario.autenticarUsuario(anyString(), anyString())).thenReturn(usuarioRetornado);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("fran"));
    }
}