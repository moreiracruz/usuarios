package br.com.moreiracruz.usuarios.controller;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_deveRetornarListaDeUsuarios() throws Exception {
        List<UsuarioDTO> usuarios = Arrays.asList(new UsuarioDTO(), new UsuarioDTO());
        when(service.findAll()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(service).findAll();
    }

    @Test
    void findById_deveRetornarUsuarioDTO() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        when(service.findById(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk());

        verify(service).findById(1L);
    }

    @Test
    void findByNome_deveRetornarUsuarioDTO() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        when(service.findByNome("joao")).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/nome/joao"))
                .andExpect(status().isOk());

        verify(service).findByNome("joao");
    }

    @Test
    void save_deveRetornarStatusCreated() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        when(service.save(any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("pawloandre"))
                .andExpect(jsonPath("$.senha").value("senha123"));

        verify(service).save(any(UsuarioDTO.class));
    }

    @Test
    void save_deveRetornarStatusBadRequest_quandoSenhaMenorQueSeisCaracteres() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"12345\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_deveRetornarStatusNoContent() throws Exception {
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void update_deveRetornarStatusOk() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        when(service.update(eq(1L), any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("pawloandre"))
                .andExpect(jsonPath("$.senha").value("senha123"));

        verify(service).update(eq(1L), any(UsuarioDTO.class));
    }
}