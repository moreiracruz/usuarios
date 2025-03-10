# Usuário API

---

[![Coverage Status](https://coveralls.io/repos/github/moreiracruz/usuarios/badge.svg?branch=main)](https://coveralls.io/github/moreiracruz/usuarios?branch=main)
[![Release](https://img.shields.io/github/v/release/moreiracruz/usuario-api)](https://github.com/moreiracruz/usuario-api/releases)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![H2 Database](https://img.shields.io/badge/H2-Database-1.4.200-blue?logo=h2)](https://www.h2database.com)
[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)](https://openjdk.org)
[![Maven](https://img.shields.io/badge/Maven-3.9.6-C71A36?logo=apachemaven)](https://maven.apache.org)
[![GitHub Actions](https://img.shields.io/github/actions/workflow/status/moreiracruz/usuarios/build.yml?logo=githubactions)](https://github.com/moreiracruz/usuarios/actions)
[![Codecov](https://img.shields.io/codecov/c/github/moreiracruz/usuarios?logo=codecov)](https://codecov.io/gh/moreiracruz/usuarios)

---

API de gerenciamento de usuários com Spring Boot e H2

## Tecnologias
- Java 17
- Spring Boot 3.2.4
- H2 Database
- JPA/Hibernate
- Maven

## Como executar
```bash
mvn spring-boot:run
```

Aqui está um exemplo completo de uma aplicação Spring Boot 3.4.3 com Java 17 que implementa uma API REST para a entidade `Usuario`. O exemplo inclui:

1. **Modelo de dados**
2. **Repository**
3. **Service**
4. **Controller**
5. **DTO**
6. **Validação**
7. **Mapper (usando MapStruct)**
8. **Testes unitários e de integração**
9. **Documentação com Swagger (SpringDoc OpenAPI)**

---

### 1. Estrutura do Projeto
```
src/main/java/com/example/demo/
├── controller/
│   └── UsuarioController.java
├── dto/
│   ├── UsuarioDTO.java
│   └── UsuarioMapper.java
├── exception/
│   └── ResourceNotFoundException.java
├── model/
│   └── Usuario.java
├── repository/
│   └── UsuarioRepository.java
├── service/
│   └── UsuarioService.java
└── DemoApplication.java
src/test/java/com/example/demo/
├── controller/
│   └── UsuarioControllerTest.java
├── service/
│   └── UsuarioServiceTest.java
└── integration/
└── UsuarioIntegrationTest.java
```

---

### 2. Dependências no `pom.xml`
Certifique-se de que as seguintes dependências estão no seu `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Banco de dados H2 (para testes) -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- SpringDoc OpenAPI (Swagger) -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>

    <!-- MapStruct (mapeamento de DTOs) -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.5.5.Final</version>
    </dependency>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>1.5.5.Final</version>
        <scope>provided</scope>
    </dependency>

    <!-- Validação -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Testes -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

### 3. Código Fonte

#### `Usuario.java` (Modelo)
```java
package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String nome;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
    private String senha;

    @CreationTimestamp
    private LocalDateTime criacao;

    @UpdateTimestamp
    private LocalDateTime edicao;
}
```

#### `UsuarioDTO.java` (DTO)
```java
package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String nome;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
    private String senha;

    private LocalDateTime criacao;
    private LocalDateTime edicao;
}
```

#### `UsuarioMapper.java` (Mapper)
```java
package com.example.demo.dto;

import com.example.demo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
```

#### `UsuarioRepository.java` (Repository)
```java
package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNome(String nome);
}
```

#### `UsuarioService.java` (Service)
```java
package com.example.demo.service;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.dto.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioMapper mapper;

    public List<UsuarioDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO findById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return mapper.toDTO(usuario);
    }

    public UsuarioDTO findByNome(String nome) {
        Usuario usuario = repository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return mapper.toDTO(usuario);
    }

    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = mapper.toEntity(usuarioDTO);
        usuario = repository.save(usuario);
        return mapper.toDTO(usuario);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario = repository.save(usuario);
        return mapper.toDTO(usuario);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
```

#### `UsuarioController.java` (Controller)
```java
package com.example.demo.controller;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuário", description = "API para gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar usuário por nome")
    public ResponseEntity<UsuarioDTO> findByNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @PostMapping
    @Operation(summary = "Criar um novo usuário")
    public ResponseEntity<UsuarioDTO> save(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário existente")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(service.update(id, usuarioDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

#### `ResourceNotFoundException.java` (Exceção)
```java
package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

### 4. Testes

#### `UsuarioServiceTest.java` (Teste Unitário)
```java
package com.example.demo.service;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    public void testFindById() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO result = service.findById(1L);
        assertEquals("Teste", result.getNome());
    }
}
```

#### `UsuarioIntegrationTest.java` (Teste de Integração)
```java
package com.example.demo.integration;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository repository;

    @Test
    public void testCreateUsuario() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Integration Test");
        usuarioDTO.setSenha("password");

        ResponseEntity<UsuarioDTO> response = restTemplate.postForEntity("/usuarios", usuarioDTO, UsuarioDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Integration Test", response.getBody().getNome());
    }
}
```

---

### 5. Documentação com Swagger
Acesse a interface do Swagger UI em:
```
http://localhost:8080/swagger-ui.html
```

---

### 6. Executando a Aplicação
1. Execute a aplicação Spring Boot.
2. Acesse o Swagger UI para testar os endpoints.
3. Execute os testes unitários e de integração.

Este exemplo cobre todos os requisitos solicitados! 😊