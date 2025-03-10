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