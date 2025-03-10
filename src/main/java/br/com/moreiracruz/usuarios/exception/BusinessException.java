package br.com.moreiracruz.usuarios.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Object ... params) {
        super(String.format(mensagem, params));
    }

}
