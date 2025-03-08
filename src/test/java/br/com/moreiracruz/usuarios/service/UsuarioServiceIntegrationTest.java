package br.com.moreiracruz.usuarios.service;

import br.com.moreiracruz.usuarios.UsuariosApplication;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = UsuariosApplication.class)
@Transactional
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void whenValidUsuario_thenShouldBeCreated() {
        Usuario usuario = new Usuario();
        usuario.setLogin("service_test");
        usuario.setPassword("service_pass");

        Usuario saved = usuarioService.criar(usuario);
        assertThat(usuarioRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void whenUpdateNonExistingUsuario_thenThrowException() {
        Usuario usuario = new Usuario();
        usuario.setLogin("not_exists");

        assertThrows(RuntimeException.class, () ->
                usuarioService.atualizar(999, usuario));
    }
}
