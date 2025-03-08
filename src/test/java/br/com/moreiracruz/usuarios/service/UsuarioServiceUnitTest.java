package br.com.moreiracruz.usuarios.service;

import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testBuscarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setLogin("newuser");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.buscarPorId(1);
        assertEquals(1, found.getId());
        assertEquals("newuser", found.getLogin());
    }

    @Test
    void testBuscarUsuarioInexistente() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(1));
    }

    @Test
    void testBuscarUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setLogin("newuser");
        List<Usuario> usuarios = List.of(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> foundry = usuarioService.listarTodos();
        assertEquals(usuarios, foundry);
    }

    @Test
    void testCriarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setLogin("newuser");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario saved = usuarioService.criar(usuario);
        assertEquals("newuser", saved.getLogin());
    }

    @Test
    void testAtualizarUsuario() {
        Usuario existing = new Usuario();
        existing.setId(1);
        existing.setLogin("olduser");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existing));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existing);

        Usuario updatedDetails = new Usuario();
        updatedDetails.setLogin("newuser");
        Usuario updated = usuarioService.atualizar(1, updatedDetails);

        assertEquals("newuser", updated.getLogin());
    }
}
