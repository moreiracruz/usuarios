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
