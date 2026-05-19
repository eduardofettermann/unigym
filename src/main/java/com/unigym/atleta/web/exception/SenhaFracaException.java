package com.unigym.atleta.web.exception;

public class SenhaFracaException extends RuntimeException {

	public SenhaFracaException() {
		super("A senha deve ter no mínimo 8 caracteres");
	}
}
