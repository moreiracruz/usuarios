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