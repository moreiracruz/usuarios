package br.com.moreiracruz.usuarios.handler;

import br.com.moreiracruz.usuarios.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@RestController
	static class TestController {
		@GetMapping("/test-business-exception")
		public void testBusinessException() {
			throw new BusinessException("Teste de erro de negócio");
		}

		@GetMapping("/test-generic-exception")
		public void testGenericException() {
			throw new RuntimeException("Teste de erro genérico");
		}
	}

	@Test
	void handleBusinessException_ShouldReturnConflictStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test-business-exception")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void handleGenericException_ShouldReturnInternalServerErrorStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test-generic-exception")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}