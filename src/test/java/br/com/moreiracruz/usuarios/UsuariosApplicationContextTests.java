package br.com.moreiracruz.usuarios;

import br.com.moreiracruz.usuarios.controller.UsuarioController;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuariosApplicationContextTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {

	}

	@Test
	void verificaContextoCarregado() {
		// Verifica se o contexto da aplicação foi criado corretamente
		assertNotNull(context, "O contexto da aplicação deve ser carregado");

		// Verifica se beans específicos estão presentes
		assertTrue(context.containsBean("usuarioController"),
				"O controller de usuário deve estar presente no contexto");

		assertTrue(context.containsBean("usuarioService"),
				"O serviço de usuário deve estar presente no contexto");
	}

	@Test
	void verificaBeansEssenciais() {
		assertNotNull(context.getBean(UsuarioRepository.class),
				"O repositório deve estar configurado");

		assertNotNull(context.getBean(UsuarioService.class),
				"O serviço deve estar configurado");

		assertNotNull(context.getBean(UsuarioController.class),
				"O controller deve estar configurado");
	}

	@Test
	void verificaConfiguracaoBancoDados() {
		DataSource dataSource = context.getBean(DataSource.class);
		assertNotNull(dataSource, "O datasource deve estar configurado");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		assertDoesNotThrow(() -> jdbcTemplate.queryForObject("SELECT 1", Integer.class),
				"Deve conseguir conectar ao banco de dados");
	}

	@Test
	void verificaHealthCheck() {
		HealthEndpoint health = context.getBean(HealthEndpoint.class);
		assertEquals(Status.UP, health.health().getStatus(),
				"O status da aplicação deve ser UP");
	}

	@Test
	void mainDeveIniciarAplicacao() {
		UsuariosApplication.main(new String[]{});
		assertThat(UsuariosApplication.class).isNotNull();
//		assertDoesNotThrow(() -> UsuariosApplication.main(new String[]{}),
//				"A execução do método main não deve lançar exceções");
	}

	@Test
	void verificaPropriedadesCarregadas(@Autowired Environment env) {
		assertEquals("sa", env.getProperty("spring.datasource.username"),
				"O username do banco deve estar configurado");

		assertEquals("true", env.getProperty("spring.h2.console.enabled"),
				"O console do H2 deve estar habilitado");
	}

//	@Test
//	void verificaConfiguracaoSeguranca() {
//		assertNotNull(context.getBean(SecurityFilterChain.class),
//				"A cadeia de filtros de segurança deve estar configurada");
//
//		assertNotNull(context.getBean(UserDetailsService.class),
//				"O serviço de detalhes de usuário deve estar configurado");
//	}

}