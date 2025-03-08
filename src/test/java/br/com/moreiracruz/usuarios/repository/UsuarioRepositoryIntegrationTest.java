package br.com.moreiracruz.usuarios.repository;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void whenFindByLogin_thenReturnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setLogin("test@example.com");
        usuario.setPassword("password");
        entityManager.persistAndFlush(usuario);

        Usuario found = usuarioRepository.findByLogin(usuario.getLogin());
        assertThat(found.getLogin()).isEqualTo(usuario.getLogin());
    }

    @Test
    void whenInvalidLogin_thenReturnNull() {
        Usuario fromDb = usuarioRepository.findByLogin("doesNotExist");
        assertThat(fromDb).isNull();
    }
}