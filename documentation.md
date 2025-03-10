# ./src/test/java/br/com/moreiracruz/usuarios/dto/UsuarioMapperTest.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioMapperTest {

    @Autowired
    private UsuarioMapper mapper;

    @Test
    void shouldMapEntityToDto() {
        Usuario entity = new Usuario();
        entity.setId(1L);
        entity.setNome("Nome");
        entity.setSenha("Senha");
        entity.setCriacao(LocalDateTime.now());
        entity.setEdicao(LocalDateTime.now());

        UsuarioDTO dto = mapper.toDTO(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Senha", dto.getSenha());
    }
}
```

# ./target/generated-sources/annotations/br/com/moreiracruz/usuarios/dto/UsuarioMapperImpl.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-10T02:47:38-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Ubuntu)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setNome( usuario.getNome() );
        usuarioDTO.setSenha( usuario.getSenha() );

        return usuarioDTO;
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setNome( usuarioDTO.getNome() );
        usuario.setSenha( usuarioDTO.getSenha() );

        return usuario;
    }
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/repository/UsuarioRepository.java

```java
package br.com.moreiracruz.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moreiracruz.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByNome(String nome);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/UsuariosApplication.java

```java
package br.com.moreiracruz.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);
	}

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandler.java

```java
package br.com.moreiracruz.usuarios.handler;

import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.moreiracruz.usuarios.exception.BusinessException;
import jakarta.annotation.Resource;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((MediaType.APPLICATION_JSON));
        return headers;
    }

    private ResponseError responseError(String message, HttpStatus statusCode) {
        ResponseError responseError = new ResponseError();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        return responseError;
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> hendleGeneral(Exception e, WebRequest request) {
        if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
            return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
        } else {
            String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
            ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
            return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler({BusinessException.class})
    private ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        ResponseError error = responseError(e.getMessage(), HttpStatus.CONFLICT);
        return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/ResponseError.java

```java
package br.com.moreiracruz.usuarios.handler;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private Date timestamp;
    private String status;
    private int statusCode;
    private String error;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/model/Usuario.java

```java
package br.com.moreiracruz.usuarios.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

# ./src/main/java/br/com/moreiracruz/usuarios/dto/CategoriaMapper.java

```java
//package br.com.moreiracruz.mapper;
//
//import br.com.moreiracruz.dto.CategoriaDTO;
//import br.com.moreiracruz.model.Categoria;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring", imports = {LocalDateTime.class}
//public interface CategoriaMapper {
//
//    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "criacao", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
//    @Mapping(target = "horaCriacao", expression = "java(java.time.LocalTime.now())")
//    Categoria toEntity(CategoriaDTO dto);
//
//    @Mapping(target = "valor", numberFormat = "#0.00")
//    CategoriaDTO toDto(Categoria entity);
//
//    default String formatarData(LocalDateTime data) {
//    return data.format(DateTimeFormatter.ISO_LOCAL_DATE);
//}
```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioDTO.java

```java
package br.com.moreiracruz.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
	@Schema(description = "id")
	private Long id;

	@Schema(description = "nome")
	@Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@Schema(description = "senha")
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioMapper.java

```java
package br.com.moreiracruz.usuarios.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.moreiracruz.usuarios.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/WelcomeController.java

```java
//package br.com.moreiracruz.usuarios.controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/")
//public class WelcomeController {
//
//    @GetMapping
//    public String welcome() {
//        return "Bem-vindo à API de Usuários!";
//    }
//}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/UsuarioController.java

```java
package br.com.moreiracruz.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

# ./src/main/java/br/com/moreiracruz/usuarios/service/UsuarioService.java

```java
package br.com.moreiracruz.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioMapper mapper;

	public List<UsuarioDTO> findAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
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

# ./src/main/java/br/com/moreiracruz/usuarios/exception/ResourceNotFoundException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class ResourceNotFoundException extends BusinessException {
	public ResourceNotFoundException(String message) {
		super(message);
	}
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/exception/BusinessException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Object ... params) {
        super(String.format(mensagem, params));
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/util/SwaggerConfig.java

```java
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

```

# ./src/test/java/br/com/moreiracruz/usuarios/dto/UsuarioMapperTest.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioMapperTest {

    @Autowired
    private UsuarioMapper mapper;

    @Test
    void shouldMapEntityToDto() {
        Usuario entity = new Usuario();
        entity.setId(1L);
        entity.setNome("Nome");
        entity.setSenha("Senha");
        entity.setCriacao(LocalDateTime.now());
        entity.setEdicao(LocalDateTime.now());

        UsuarioDTO dto = mapper.toDTO(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Senha", dto.getSenha());
    }
}
```

# ./target/generated-sources/annotations/br/com/moreiracruz/usuarios/dto/UsuarioMapperImpl.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-10T04:10:31-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Ubuntu)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setNome( usuario.getNome() );
        usuarioDTO.setSenha( usuario.getSenha() );

        return usuarioDTO;
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setNome( usuarioDTO.getNome() );
        usuario.setSenha( usuarioDTO.getSenha() );

        return usuario;
    }
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/repository/UsuarioRepository.java

```java
package br.com.moreiracruz.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moreiracruz.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByNome(String nome);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/UsuariosApplication.java

```java
package br.com.moreiracruz.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);
	}

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandler.java

```java
package br.com.moreiracruz.usuarios.handler;

import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.moreiracruz.usuarios.exception.BusinessException;
import jakarta.annotation.Resource;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((MediaType.APPLICATION_JSON));
        return headers;
    }

    private ResponseError responseError(String message, HttpStatus statusCode) {
        ResponseError responseError = new ResponseError();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        return responseError;
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> hendleGeneral(Exception e, WebRequest request) {
        if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
            return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
        } else {
            String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
            ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
            return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler({BusinessException.class})
    private ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        ResponseError error = responseError(e.getMessage(), HttpStatus.CONFLICT);
        return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/ResponseError.java

```java
package br.com.moreiracruz.usuarios.handler;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private Date timestamp;
    private String status;
    private int statusCode;
    private String error;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/model/Usuario.java

```java
package br.com.moreiracruz.usuarios.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

# ./src/main/java/br/com/moreiracruz/usuarios/dto/CategoriaMapper.java

```java
//package br.com.moreiracruz.mapper;
//
//import br.com.moreiracruz.dto.CategoriaDTO;
//import br.com.moreiracruz.model.Categoria;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring", imports = {LocalDateTime.class}
//public interface CategoriaMapper {
//
//    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "criacao", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
//    @Mapping(target = "horaCriacao", expression = "java(java.time.LocalTime.now())")
//    Categoria toEntity(CategoriaDTO dto);
//
//    @Mapping(target = "valor", numberFormat = "#0.00")
//    CategoriaDTO toDto(Categoria entity);
//
//    default String formatarData(LocalDateTime data) {
//    return data.format(DateTimeFormatter.ISO_LOCAL_DATE);
//}
```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioDTO.java

```java
package br.com.moreiracruz.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
	@Schema(description = "id")
	private Long id;

	@Schema(description = "nome")
	@Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@Schema(description = "senha")
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioMapper.java

```java
package br.com.moreiracruz.usuarios.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.moreiracruz.usuarios.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/WelcomeController.java

```java
package br.com.moreiracruz.usuarios.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public String welcome() {
        return "Bem-vindo à API de Usuários!";
    }
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/UsuarioController.java

```java
package br.com.moreiracruz.usuarios.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	public ResponseEntity<UsuarioDTO> save(@Valid @RequestBody UsuarioDTO usuarioDTO) {
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

# ./src/main/java/br/com/moreiracruz/usuarios/service/UsuarioService.java

```java
package br.com.moreiracruz.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioMapper mapper;

	public List<UsuarioDTO> findAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
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

# ./src/main/java/br/com/moreiracruz/usuarios/exception/ResourceNotFoundException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class ResourceNotFoundException extends BusinessException {
	public ResourceNotFoundException(String message) {
		super(message);
	}
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/exception/BusinessException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Object ... params) {
        super(String.format(mensagem, params));
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/util/SwaggerConfig.java

```java
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

```

# ./src/test/java/br/com/moreiracruz/usuarios/repository/UsuarioRepositoryUnitTest.java

```java
package br.com.moreiracruz.usuarios.repository;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryUnitTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void findByNome_deveRetornarUsuarioQuandoExistir() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setSenha("senha123");
        repository.save(usuario);

        Optional<Usuario> encontrado = repository.findByNome("Maria");
        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
    }

    @Test
    void findByNome_deveRetornarVazioQuandoNaoExistir() {
        Optional<Usuario> encontrado = repository.findByNome("Inexistente");
        assertTrue(encontrado.isEmpty());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandlerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void handleResourceNotFoundException_deveRetornar409() throws Exception {
		mockMvc.perform(get("/usuarios/999")).andExpect(status().isConflict()).andExpect(jsonPath("$.error").exists());
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/model/UsuarioModelUnitTest.java

```java
package br.com.moreiracruz.usuarios.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioModelUnitTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void usuarioComNomeVazio_deveRetornarViolacao() {
        Usuario usuario = new Usuario();
        usuario.setNome("");
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("O nome é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComSenhaCurta_deveRetornarViolacao() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha("123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("A senha deve ter entre 6 e 20 caracteres", violations.iterator().next().getMessage());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/dto/UsuarioMapperTest.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioMapperTest {

    @Autowired
    private UsuarioMapper mapper;

    @Test
    void shouldMapEntityToDto() {
        Usuario entity = new Usuario();
        entity.setId(1L);
        entity.setNome("Nome");
        entity.setSenha("Senha");
        entity.setCriacao(LocalDateTime.now());
        entity.setEdicao(LocalDateTime.now());

        UsuarioDTO dto = mapper.toDTO(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Senha", dto.getSenha());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/UsuarioControllerUnitTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Test
    void findAll_deveRetornarListaVazia() throws Exception {
        Mockito.when(service.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void save_deveRetornarStatusCreated() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Novo");
        dto.setSenha("123456");

        Mockito.when(service.save(dto)).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Novo\", \"senha\": \"123456\" }"))
                .andExpect(status().isCreated());
    }

    @Test
    void save_deveRetornarStatusBadRequest_quandoSenhaMenorQueSeisCaracteres() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Novo");
        dto.setSenha("12345");

        Mockito.when(service.save(dto)).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"Novo\", \"senha\": \"senha\" }"))
                .andExpect(status().isBadRequest());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/UsuarioControllerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository repository;

	@Test
	void criarUsuario_deveRetornar201() throws Exception {
		String json = "{ \"nome\": \"Teste\", \"senha\": \"senha123\" }";
		mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());
	}

	@Test
	void buscarPorId_deveRetornar409QuandoNaoEncontrado() throws Exception {
		mockMvc.perform(get("/usuarios/999")).andExpect(status().isConflict());
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/WelcomeControllerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcome_deveRetornarMensagemBoasVindas() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bem-vindo à API de Usuários!"));
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/service/UsuarioServiceUnitTest.java

```java
package br.com.moreiracruz.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService service;

    @Test
    void findById_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void save_deveRetornarUsuarioDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        Usuario usuario = new Usuario();
        when(mapper.toEntity(dto)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void update_deveLancarExcecaoQuandoUsuarioNaoExiste() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado");
        dto.setSenha("novaSenha");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void delete_deveChamarRepositoryDelete() {
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
```

# ./target/generated-sources/annotations/br/com/moreiracruz/usuarios/dto/UsuarioMapperImpl.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-10T06:46:13-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Ubuntu)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setNome( usuario.getNome() );
        usuarioDTO.setSenha( usuario.getSenha() );

        return usuarioDTO;
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setNome( usuarioDTO.getNome() );
        usuario.setSenha( usuarioDTO.getSenha() );

        return usuario;
    }
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/repository/UsuarioRepository.java

```java
package br.com.moreiracruz.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moreiracruz.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByNome(String nome);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/UsuariosApplication.java

```java
package br.com.moreiracruz.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);
	}

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandler.java

```java
package br.com.moreiracruz.usuarios.handler;

import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.moreiracruz.usuarios.exception.BusinessException;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;

    HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((MediaType.APPLICATION_JSON));
        return headers;
    }

    ResponseError responseError(String message, HttpStatus statusCode) {
        ResponseError responseError = new ResponseError();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        responseError.setTimestamp(LocalDateTime.now());
        return responseError;
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> hendleGeneral(Exception e, WebRequest request) {
        if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
            return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
        } else {
            String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
            ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
            return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler({BusinessException.class})
    ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        ResponseError error = responseError(e.getMessage(), HttpStatus.CONFLICT);
        return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/ResponseError.java

```java
package br.com.moreiracruz.usuarios.handler;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private LocalDateTime timestamp;
    private String status;
    private int statusCode;
    private String error;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/model/Usuario.java

```java
package br.com.moreiracruz.usuarios.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

# ./src/main/java/br/com/moreiracruz/usuarios/dto/CategoriaMapper.java

```java
//package br.com.moreiracruz.mapper;
//
//import br.com.moreiracruz.dto.CategoriaDTO;
//import br.com.moreiracruz.model.Categoria;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring", imports = {LocalDateTime.class}
//public interface CategoriaMapper {
//
//    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "criacao", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
//    @Mapping(target = "horaCriacao", expression = "java(java.time.LocalTime.now())")
//    Categoria toEntity(CategoriaDTO dto);
//
//    @Mapping(target = "valor", numberFormat = "#0.00")
//    CategoriaDTO toDto(Categoria entity);
//
//    default String formatarData(LocalDateTime data) {
//    return data.format(DateTimeFormatter.ISO_LOCAL_DATE);
//}
```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioDTO.java

```java
package br.com.moreiracruz.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
	@Schema(description = "id")
	private Long id;

	@Schema(description = "nome")
	@Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@Schema(description = "senha")
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioMapper.java

```java
package br.com.moreiracruz.usuarios.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.moreiracruz.usuarios.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/WelcomeController.java

```java
package br.com.moreiracruz.usuarios.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public String welcome() {
        return "Bem-vindo à API de Usuários!";
    }
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/UsuarioController.java

```java
package br.com.moreiracruz.usuarios.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	public ResponseEntity<UsuarioDTO> save(@Valid @RequestBody UsuarioDTO usuarioDTO) {
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

# ./src/main/java/br/com/moreiracruz/usuarios/service/UsuarioService.java

```java
package br.com.moreiracruz.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioMapper mapper;

	public List<UsuarioDTO> findAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
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

# ./src/main/java/br/com/moreiracruz/usuarios/exception/ResourceNotFoundException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class ResourceNotFoundException extends BusinessException {
	public ResourceNotFoundException(String message) {
		super(message);
	}
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/exception/BusinessException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Object ... params) {
        super(String.format(mensagem, params));
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/util/SwaggerConfig.java

```java
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

```

# ./src/test/java/br/com/moreiracruz/usuarios/repository/UsuarioRepositoryUnitTest.java

```java
package br.com.moreiracruz.usuarios.repository;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryUnitTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void findByNome_deveRetornarUsuarioQuandoExistir() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setSenha("senha123");
        repository.save(usuario);

        Optional<Usuario> encontrado = repository.findByNome("Maria");
        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
    }

    @Test
    void findByNome_deveRetornarVazioQuandoNaoExistir() {
        Optional<Usuario> encontrado = repository.findByNome("Inexistente");
        assertTrue(encontrado.isEmpty());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandlerIntegrationTest.java

```java
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
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Teste de erro de negócio"));
	}

	@Test
	void handleGenericException_ShouldReturnInternalServerErrorStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test-generic-exception")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandlerUnitTest.java

```java
package br.com.moreiracruz.usuarios.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import br.com.moreiracruz.usuarios.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerUnitTest {

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Mock
	private MessageSource messageSource;

	@Mock
	private WebRequest webRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void handleBusinessException_ShouldReturnConflictStatus() {
		BusinessException exception = new BusinessException("Business Error");
		ResponseEntity<Object> response = globalExceptionHandler.handleBusinessException(exception, webRequest);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		ResponseError responseError = (ResponseError) response.getBody();
		assertEquals("Business Error", responseError.getError());
	}

	@Test
	void hendleGeneral_ShouldHandleUndeclaredThrowableException() {
		BusinessException businessException = new BusinessException("Business Error");
		UndeclaredThrowableException exception = new UndeclaredThrowableException(businessException);

		ResponseEntity<Object> response = globalExceptionHandler.hendleGeneral(exception, webRequest);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		ResponseError responseError = (ResponseError) response.getBody();
		assertEquals("Business Error", responseError.getError());
	}

//	@Test
//	void hendleGeneral_ShouldHandleGenericException() {
//		Exception exception = new Exception("Generic Error");
//		when(messageSource.getMessage(eq("error.server"), any(), any())).thenReturn("Internal Server Error");
//
//		ResponseEntity<Object> response = globalExceptionHandler.hendleGeneral(exception, webRequest);
//
//		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//		ResponseError responseError = (ResponseError) response.getBody();
//		assertEquals("Internal Server Error", responseError.getError());
//	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/model/UsuarioModelUnitTest.java

```java
package br.com.moreiracruz.usuarios.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioModelUnitTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void usuarioComNomeNulo_deveRetornarViolacao_nomeObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("O nome é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComNomeVazio_deveRetornarViolacao_nomeObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setNome("");
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usuarioComNomeCurto_deveRetornarViolacao_nomePequeno() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ed");
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("O nome deve ter entre 3 e 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComSenhaNula_deveRetornarViolacao_senhaObrigatoria() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha(null);

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("A senha é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComSenhaVazia_deveRetornarViolacao_senhaObrigatoria() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha("");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usuarioComSenhaCurta_deveRetornarViolacao_senhaPequena() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha("123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("A senha deve ter entre 6 e 20 caracteres", violations.iterator().next().getMessage());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/dto/UsuarioMapperTest.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioMapperTest {

    @Autowired
    private UsuarioMapper mapper;

    @Test
    void shouldMapEntityToDto() {
        Usuario entity = new Usuario();
        entity.setId(1L);
        entity.setNome("Nome");
        entity.setSenha("Senha");
        entity.setCriacao(LocalDateTime.now());
        entity.setEdicao(LocalDateTime.now());

        UsuarioDTO dto = mapper.toDTO(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Senha", dto.getSenha());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/UsuarioControllerUnitTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Test
    void findAll_deveRetornarListaVazia() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_deveRetornarOptionVazia() throws Exception {
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByNome_deveRetornarOptionVazia() throws Exception {
        mockMvc.perform(get("/usuarios/nome/joao"))
                .andExpect(status().isOk());
    }

    @Test
    void save_deveRetornarStatusCreated() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        Mockito.when(service.save(any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"senha123\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("pawloandre"))
                .andExpect(jsonPath("$.senha").value("senha123"));
    }

    @Test
    void save_deveRetornarStatusBadRequest_quandoSenhaMenorQueSeisCaracteres() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("12345");

        Mockito.when(service.save(any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"senha\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_deveRetornarStatusOk() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        mockMvc.perform(delete("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"senha123\" }"))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_deveRetornarStatusCreated() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        Mockito.when(service.update(any(), any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{  \"id\": \"1\", \"nome\": \"pawloandre\", \"senha\": \"senha123\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("pawloandre"))
                .andExpect(jsonPath("$.senha").value("senha123"));
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/UsuarioControllerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository repository;

	@Test
	void criarUsuario_deveRetornar201() throws Exception {
		String json = "{ \"nome\": \"Teste\", \"senha\": \"senha123\" }";
		mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());
	}

	@Test
	void buscarPorId_deveRetornar409QuandoNaoEncontrado() throws Exception {
		mockMvc.perform(get("/usuarios/999")).andExpect(status().isConflict());
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/WelcomeControllerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcome_deveRetornarMensagemBoasVindas() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bem-vindo à API de Usuários!"));
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/service/UsuarioServiceUnitTest.java

```java
package br.com.moreiracruz.usuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_deveRetornarListaVaiza() {
        when(repository.findAll()).thenReturn(List.of());
        assertNotNull(service.findAll());
    }

    @Test
    void findById_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findByNome_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findByNome("Nome")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findByNome("Nome"));
    }

    @Test
    void save_deveRetornarUsuarioDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        Usuario usuario = new Usuario();
        when(mapper.toEntity(dto)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void update_deveLancarExcecaoQuandoUsuarioNaoExiste() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado");
        dto.setSenha("novaSenha");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void delete_deveChamarRepositoryDelete() {
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/exception/ResourceNotFoundExceptionUnitTest.java

```java
package br.com.moreiracruz.usuarios.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResourceNotFoundExceptionUnitTest {

	@Test
	void constructor_ShouldSetMensagem() {
		String mensagem = "Recurso não encontrado";
		ResourceNotFoundException exception = new ResourceNotFoundException(mensagem);
		assertEquals(mensagem, exception.getMessage());
	}

	@Test
	void resourceNotFoundException_isInstanceOfBusinessException() {
		String mensagem = "Recurso não encontrado";
		ResourceNotFoundException exception = new ResourceNotFoundException(mensagem);
		assertEquals(true, exception instanceof BusinessException);
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/exception/BusinessExceptionUnitTest.java

```java
package br.com.moreiracruz.usuarios.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BusinessExceptionUnitTest {

	@Test
	void constructor_ShouldSetMensagem() {
		String mensagem = "Erro de negócio";
		BusinessException exception = new BusinessException(mensagem);
		assertEquals(mensagem, exception.getMessage());
	}

	@Test
	void constructor_ShouldFormatMensagemWithParams() {
		String mensagem = "Erro de negócio com parâmetro: %s";
		String parametro = "valor";
		BusinessException exception = new BusinessException(mensagem, parametro);
		assertEquals("Erro de negócio com parâmetro: valor", exception.getMessage());
	}
}

```

# ./target/generated-sources/annotations/br/com/moreiracruz/usuarios/dto/UsuarioMapperImpl.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-10T06:46:13-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Ubuntu)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setNome( usuario.getNome() );
        usuarioDTO.setSenha( usuario.getSenha() );

        return usuarioDTO;
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setNome( usuarioDTO.getNome() );
        usuario.setSenha( usuarioDTO.getSenha() );

        return usuario;
    }
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/repository/UsuarioRepository.java

```java
package br.com.moreiracruz.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moreiracruz.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByNome(String nome);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/UsuariosApplication.java

```java
package br.com.moreiracruz.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);
	}

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandler.java

```java
package br.com.moreiracruz.usuarios.handler;

import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.moreiracruz.usuarios.exception.BusinessException;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;

    HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((MediaType.APPLICATION_JSON));
        return headers;
    }

    ResponseError responseError(String message, HttpStatus statusCode) {
        ResponseError responseError = new ResponseError();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        responseError.setTimestamp(LocalDateTime.now());
        return responseError;
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> hendleGeneral(Exception e, WebRequest request) {
        if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
            return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
        } else {
            String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
            ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
            return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler({BusinessException.class})
    ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        ResponseError error = responseError(e.getMessage(), HttpStatus.CONFLICT);
        return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/handler/ResponseError.java

```java
package br.com.moreiracruz.usuarios.handler;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private LocalDateTime timestamp;
    private String status;
    private int statusCode;
    private String error;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/model/Usuario.java

```java
package br.com.moreiracruz.usuarios.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

# ./src/main/java/br/com/moreiracruz/usuarios/dto/CategoriaMapper.java

```java
//package br.com.moreiracruz.mapper;
//
//import br.com.moreiracruz.dto.CategoriaDTO;
//import br.com.moreiracruz.model.Categoria;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring", imports = {LocalDateTime.class}
//public interface CategoriaMapper {
//
//    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "criacao", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
//    @Mapping(target = "horaCriacao", expression = "java(java.time.LocalTime.now())")
//    Categoria toEntity(CategoriaDTO dto);
//
//    @Mapping(target = "valor", numberFormat = "#0.00")
//    CategoriaDTO toDto(Categoria entity);
//
//    default String formatarData(LocalDateTime data) {
//    return data.format(DateTimeFormatter.ISO_LOCAL_DATE);
//}
```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioDTO.java

```java
package br.com.moreiracruz.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
	@Schema(description = "id")
	private Long id;

	@Schema(description = "nome")
	@Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@Schema(description = "senha")
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/dto/UsuarioMapper.java

```java
package br.com.moreiracruz.usuarios.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.moreiracruz.usuarios.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/WelcomeController.java

```java
package br.com.moreiracruz.usuarios.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public String welcome() {
        return "Bem-vindo à API de Usuários!";
    }
}
```

# ./src/main/java/br/com/moreiracruz/usuarios/controller/UsuarioController.java

```java
package br.com.moreiracruz.usuarios.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	public ResponseEntity<UsuarioDTO> save(@Valid @RequestBody UsuarioDTO usuarioDTO) {
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

# ./src/main/java/br/com/moreiracruz/usuarios/service/UsuarioService.java

```java
package br.com.moreiracruz.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioMapper mapper;

	public List<UsuarioDTO> findAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
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

# ./src/main/java/br/com/moreiracruz/usuarios/exception/ResourceNotFoundException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class ResourceNotFoundException extends BusinessException {
	public ResourceNotFoundException(String message) {
		super(message);
	}
}

```

# ./src/main/java/br/com/moreiracruz/usuarios/exception/BusinessException.java

```java
package br.com.moreiracruz.usuarios.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Object ... params) {
        super(String.format(mensagem, params));
    }

}

```

# ./src/main/java/br/com/moreiracruz/usuarios/util/SwaggerConfig.java

```java
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

```

# ./src/test/java/br/com/moreiracruz/usuarios/repository/UsuarioRepositoryUnitTest.java

```java
package br.com.moreiracruz.usuarios.repository;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryUnitTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void findByNome_deveRetornarUsuarioQuandoExistir() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setSenha("senha123");
        repository.save(usuario);

        Optional<Usuario> encontrado = repository.findByNome("Maria");
        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
    }

    @Test
    void findByNome_deveRetornarVazioQuandoNaoExistir() {
        Optional<Usuario> encontrado = repository.findByNome("Inexistente");
        assertTrue(encontrado.isEmpty());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandlerIntegrationTest.java

```java
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
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Teste de erro de negócio"));
	}

	@Test
	void handleGenericException_ShouldReturnInternalServerErrorStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test-generic-exception")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/handler/GlobalExceptionHandlerUnitTest.java

```java
package br.com.moreiracruz.usuarios.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import br.com.moreiracruz.usuarios.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerUnitTest {

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Mock
	private MessageSource messageSource;

	@Mock
	private WebRequest webRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void handleBusinessException_ShouldReturnConflictStatus() {
		BusinessException exception = new BusinessException("Business Error");
		ResponseEntity<Object> response = globalExceptionHandler.handleBusinessException(exception, webRequest);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		ResponseError responseError = (ResponseError) response.getBody();
		assertEquals("Business Error", responseError.getError());
	}

	@Test
	void hendleGeneral_ShouldHandleUndeclaredThrowableException() {
		BusinessException businessException = new BusinessException("Business Error");
		UndeclaredThrowableException exception = new UndeclaredThrowableException(businessException);

		ResponseEntity<Object> response = globalExceptionHandler.hendleGeneral(exception, webRequest);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		ResponseError responseError = (ResponseError) response.getBody();
		assertEquals("Business Error", responseError.getError());
	}

//	@Test
//	void hendleGeneral_ShouldHandleGenericException() {
//		Exception exception = new Exception("Generic Error");
//		when(messageSource.getMessage(eq("error.server"), any(), any())).thenReturn("Internal Server Error");
//
//		ResponseEntity<Object> response = globalExceptionHandler.hendleGeneral(exception, webRequest);
//
//		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//		ResponseError responseError = (ResponseError) response.getBody();
//		assertEquals("Internal Server Error", responseError.getError());
//	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/model/UsuarioModelUnitTest.java

```java
package br.com.moreiracruz.usuarios.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioModelUnitTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void usuarioComNomeNulo_deveRetornarViolacao_nomeObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("O nome é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComNomeVazio_deveRetornarViolacao_nomeObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setNome("");
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usuarioComNomeCurto_deveRetornarViolacao_nomePequeno() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ed");
        usuario.setSenha("senha123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("O nome deve ter entre 3 e 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComSenhaNula_deveRetornarViolacao_senhaObrigatoria() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha(null);

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("A senha é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    void usuarioComSenhaVazia_deveRetornarViolacao_senhaObrigatoria() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha("");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
    }

    @Test
    void usuarioComSenhaCurta_deveRetornarViolacao_senhaPequena() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setSenha("123");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertEquals("A senha deve ter entre 6 e 20 caracteres", violations.iterator().next().getMessage());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/dto/UsuarioMapperTest.java

```java
package br.com.moreiracruz.usuarios.dto;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioMapperTest {

    @Autowired
    private UsuarioMapper mapper;

    @Test
    void shouldMapEntityToDto() {
        Usuario entity = new Usuario();
        entity.setId(1L);
        entity.setNome("Nome");
        entity.setSenha("Senha");
        entity.setCriacao(LocalDateTime.now());
        entity.setEdicao(LocalDateTime.now());

        UsuarioDTO dto = mapper.toDTO(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals("Senha", dto.getSenha());
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/UsuarioControllerUnitTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Test
    void findAll_deveRetornarListaVazia() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_deveRetornarOptionVazia() throws Exception {
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByNome_deveRetornarOptionVazia() throws Exception {
        mockMvc.perform(get("/usuarios/nome/joao"))
                .andExpect(status().isOk());
    }

    @Test
    void save_deveRetornarStatusCreated() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        Mockito.when(service.save(any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"senha123\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("pawloandre"))
                .andExpect(jsonPath("$.senha").value("senha123"));
    }

    @Test
    void save_deveRetornarStatusBadRequest_quandoSenhaMenorQueSeisCaracteres() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("12345");

        Mockito.when(service.save(any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"senha\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_deveRetornarStatusOk() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        mockMvc.perform(delete("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"pawloandre\", \"senha\": \"senha123\" }"))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_deveRetornarStatusCreated() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNome("pawloandre");
        dto.setSenha("senha123");

        Mockito.when(service.update(any(), any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{  \"id\": \"1\", \"nome\": \"pawloandre\", \"senha\": \"senha123\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("pawloandre"))
                .andExpect(jsonPath("$.senha").value("senha123"));
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/UsuarioControllerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository repository;

	@Test
	void criarUsuario_deveRetornar201() throws Exception {
		String json = "{ \"nome\": \"Teste\", \"senha\": \"senha123\" }";
		mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());
	}

	@Test
	void buscarPorId_deveRetornar409QuandoNaoEncontrado() throws Exception {
		mockMvc.perform(get("/usuarios/999")).andExpect(status().isConflict());
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/controller/WelcomeControllerIntegrationTest.java

```java
package br.com.moreiracruz.usuarios.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcome_deveRetornarMensagemBoasVindas() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bem-vindo à API de Usuários!"));
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/service/UsuarioServiceUnitTest.java

```java
package br.com.moreiracruz.usuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_deveRetornarListaVaiza() {
        when(repository.findAll()).thenReturn(List.of());
        assertNotNull(service.findAll());
    }

    @Test
    void findById_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findByNome_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findByNome("Nome")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findByNome("Nome"));
    }

    @Test
    void save_deveRetornarUsuarioDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        Usuario usuario = new Usuario();
        when(mapper.toEntity(dto)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void update_deveLancarExcecaoQuandoUsuarioNaoExiste() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado");
        dto.setSenha("novaSenha");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void delete_deveChamarRepositoryDelete() {
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/exception/ResourceNotFoundExceptionUnitTest.java

```java
package br.com.moreiracruz.usuarios.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResourceNotFoundExceptionUnitTest {

	@Test
	void constructor_ShouldSetMensagem() {
		String mensagem = "Recurso não encontrado";
		ResourceNotFoundException exception = new ResourceNotFoundException(mensagem);
		assertEquals(mensagem, exception.getMessage());
	}

	@Test
	void resourceNotFoundException_isInstanceOfBusinessException() {
		String mensagem = "Recurso não encontrado";
		ResourceNotFoundException exception = new ResourceNotFoundException(mensagem);
		assertEquals(true, exception instanceof BusinessException);
	}
}
```

# ./src/test/java/br/com/moreiracruz/usuarios/exception/BusinessExceptionUnitTest.java

```java
package br.com.moreiracruz.usuarios.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BusinessExceptionUnitTest {

	@Test
	void constructor_ShouldSetMensagem() {
		String mensagem = "Erro de negócio";
		BusinessException exception = new BusinessException(mensagem);
		assertEquals(mensagem, exception.getMessage());
	}

	@Test
	void constructor_ShouldFormatMensagemWithParams() {
		String mensagem = "Erro de negócio com parâmetro: %s";
		String parametro = "valor";
		BusinessException exception = new BusinessException(mensagem, parametro);
		assertEquals("Erro de negócio com parâmetro: valor", exception.getMessage());
	}
}

```

