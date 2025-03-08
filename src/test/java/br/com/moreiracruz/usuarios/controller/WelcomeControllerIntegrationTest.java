package br.com.moreiracruz.usuarios.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WelcomeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void welcome_DeveRetornarStatusOkEMensagemCorreta() {
        var response = restTemplate.getForEntity("/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Bem-vindo à API de Usuários!");
    }

    @Test
    void welcome_DeveConterCabecalhosCorretos() {
        var headers = restTemplate.headForHeaders("/");

        assertThat(headers.getContentType())
                .isNotNull()
                .hasToString("text/plain;charset=UTF-8");
    }
}