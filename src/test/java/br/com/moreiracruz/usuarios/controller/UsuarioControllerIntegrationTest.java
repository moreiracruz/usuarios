package br.com.moreiracruz.usuarios.controller;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	private Usuario usuario;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
		usuario = new Usuario();
		usuario.setNome("Teste");
		usuario.setSenha("senha123");
		usuario = repository.save(usuario);
	}

	@Test
	void criarUsuario_deveRetornar201() throws Exception {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNome("Novo Usuario");
		dto.setSenha("senha456");

		mockMvc.perform(post("/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nome").value("Novo Usuario"));
	}

	@Test
	void buscarPorId_deveRetornar200QuandoEncontrado() throws Exception {
		mockMvc.perform(get("/usuarios/" + usuario.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Teste"));
	}

	@Test
	void buscarPorId_deveRetornar404QuandoNaoEncontrado() throws Exception {
		mockMvc.perform(get("/usuarios/999"))
				.andExpect(status().isConflict());
	}

	@Test
	void atualizarUsuario_deveRetornar200() throws Exception {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNome("Teste Atualizado");
		dto.setSenha("senha789");

		mockMvc.perform(put("/usuarios/" + usuario.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Teste Atualizado"));
	}

	@Test
	void deletarUsuario_deveRetornar204() throws Exception {
		mockMvc.perform(delete("/usuarios/" + usuario.getId()))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/usuarios/" + usuario.getId()))
				.andExpect(status().isConflict());
	}
}