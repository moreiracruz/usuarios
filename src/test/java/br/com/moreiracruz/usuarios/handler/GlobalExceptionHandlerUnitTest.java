package br.com.moreiracruz.usuarios.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import br.com.moreiracruz.usuarios.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerUnitTest {

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Mock
	private MessageSource messageSource;

	@Mock
	private WebRequest webRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void handleBusinessException_ShouldReturnConflictStatus() {
		BusinessException exception = new BusinessException("Business Error");
		ResponseEntity<Object> response = globalExceptionHandler.handleBusinessException(exception, webRequest);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		ResponseError responseError = (ResponseError) response.getBody();
		assertEquals("Business Error", responseError.getError());
	}

	@Test
	void hendleGeneral_ShouldHandleUndeclaredThrowableException() {
		BusinessException businessException = new BusinessException("Business Error");
		UndeclaredThrowableException exception = new UndeclaredThrowableException(businessException);

		ResponseEntity<Object> response = globalExceptionHandler.hendleGeneral(exception, webRequest);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		ResponseError responseError = (ResponseError) response.getBody();
		assertEquals("Business Error", responseError.getError());
	}

//	@Test
//	void hendleGeneral_ShouldHandleGenericException() {
//		Exception exception = new Exception("Generic Error");
//		when(messageSource.getMessage(eq("error.server"), any(), any())).thenReturn("Internal Server Error");
//
//		ResponseEntity<Object> response = globalExceptionHandler.hendleGeneral(exception, webRequest);
//
//		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//		ResponseError responseError = (ResponseError) response.getBody();
//		assertEquals("Internal Server Error", responseError.getError());
//	}
}