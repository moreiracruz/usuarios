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