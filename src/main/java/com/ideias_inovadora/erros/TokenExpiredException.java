package com.ideias_inovadora.erros;

/**
 * Exceção lançada quando o token de autenticação expira.
 */
public class TokenExpiredException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Construtor que recebe uma mensagem personalizada.
     * 
     * @param message Mensagem explicando o motivo do erro.
     */
    public TokenExpiredException(String message) {
        super(message);
    }
}
