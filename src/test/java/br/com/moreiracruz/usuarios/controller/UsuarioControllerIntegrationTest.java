package br.com.moreiracruz.usuarios.controller;

import br.com.moreiracruz.usuarios.UsuariosApplication;
import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UsuariosApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setLogin("test_integration");
        usuario.setPassword("integration_pass");
    }

    @Test
    void testFullCrudCycle() {
        // Create
        ResponseEntity<Usuario> createResponse = restTemplate.postForEntity(
                "/usuarios", usuario, Usuario.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Usuario created = createResponse.getBody();
        assertThat(created.getId()).isNotNull();

        // Read
        ResponseEntity<Usuario> getResponse = restTemplate.getForEntity(
                "/usuarios/" + created.getId(), Usuario.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getLogin()).isEqualTo("test_integration");

        // Update
        created.setPassword("new_password");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<Usuario> requestEntity = new RequestEntity<>(
                created, headers, HttpMethod.PUT, URI.create("/usuarios/" + created.getId()));

        ResponseEntity<Usuario> updateResponse = restTemplate.exchange(
                requestEntity, Usuario.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getPassword()).isEqualTo("new_password");

        // Delete
        restTemplate.delete("/usuarios/" + created.getId());

        ResponseEntity<Usuario> finalGet = restTemplate.getForEntity(
                "/usuarios/" + created.getId(), Usuario.class);
        assertThat(finalGet.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
