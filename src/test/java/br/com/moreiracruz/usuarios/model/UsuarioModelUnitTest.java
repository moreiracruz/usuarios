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