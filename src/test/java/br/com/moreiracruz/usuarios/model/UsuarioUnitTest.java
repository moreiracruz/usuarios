package br.com.moreiracruz.usuarios.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioUnitTest {
    @Test
    void testUsuarioGettersAndSetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setLogin("testuser");
        usuario.setPassword("password");

        assertEquals(1, usuario.getId());
        assertEquals("testuser", usuario.getLogin());
        assertEquals("password", usuario.getPassword());
    }
}
