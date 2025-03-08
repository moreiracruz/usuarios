package br.com.moreiracruz.usuarios.repository;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryUnitTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testSaveAndFindUsuario() {
        Usuario usuario = new Usuario();
        usuario.setLogin("testuser");
        usuario.setPassword("secret");

        Usuario saved = usuarioRepository.save(usuario);
        Usuario found = usuarioRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("testuser", found.getLogin());
    }
}
