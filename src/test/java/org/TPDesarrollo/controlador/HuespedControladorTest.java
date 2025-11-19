package org.TPDesarrollo.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.TPDesarrollo.dtos.HuespedDTO;
import org.TPDesarrollo.exceptions.DniExistente;
import org.TPDesarrollo.gestores.GestorHuesped;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HuespedControlador.class)
@AutoConfigureMockMvc(addFilters = false)
class HuespedControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GestorHuesped gestorHuesped;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void alta_DeberiaDevolver409_SiDniExiste() throws Exception {
        HuespedDTO dto = new HuespedDTO();

        dto.setNombre("Test");
        dto.setApellido("Test");
        dto.setDocumento("11111111");
        // ... llenar el resto si @Valid salta ...

        // Simulamos que el Gestor lanza la excepción de negocio
        when(gestorHuesped.darDeAltaHuesped(any(HuespedDTO.class)))
                .thenThrow(new DniExistente("DNI Duplicado"));

        mockMvc.perform(post("/api/huespedes/alta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict()); // Esperamos 409 Conflict
    }
}