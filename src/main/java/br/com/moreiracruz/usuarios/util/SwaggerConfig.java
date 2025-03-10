package br.com.moreiracruz.usuarios.util;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuários")
                        .version("1.0.0")
                        .description("API para gerenciamento de usuários")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@example.com")
                                .url("https://github.com/moreiracruz"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
    // Adicione na classe SwaggerConfig
    @Bean
    public GroupedOpenApi usuariosApi() {
        return GroupedOpenApi.builder()
                .group("usuarios") // Nome do grupo (pode ser qualquer um)
                .packagesToScan("br.com.moreiracruz.usuarios.controller") // Pacote dos controladores
                .build();
    }
}
