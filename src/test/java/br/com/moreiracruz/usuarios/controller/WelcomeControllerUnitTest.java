package br.com.moreiracruz.usuarios.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WelcomeController.class)
class WelcomeControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcome_DeveRetornarMensagemBoasVindas() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bem-vindo à API de Usuários!"));
    }

    @Test
    void welcome_DeveRetornarContentTypeTexto() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(content().contentTypeCompatibleWith("text/plain"));
    }

    @Test
    void endpointInexistente_DeveRetornarNotFound() throws Exception {
        mockMvc.perform(get("/nao-existo"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postNoEndpointRoot_DeveRetornarMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/"))
                .andExpect(status().isMethodNotAllowed());
    }

}