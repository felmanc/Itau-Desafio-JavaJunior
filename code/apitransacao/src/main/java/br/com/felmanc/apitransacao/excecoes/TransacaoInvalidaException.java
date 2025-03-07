package br.com.felmanc.apitransacao.excecoes;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TransacaoInvalidaException extends ResponseStatusException {
	private static final long serialVersionUID = 1L;

	public TransacaoInvalidaException(String mensagem) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, mensagem);
    }
}
