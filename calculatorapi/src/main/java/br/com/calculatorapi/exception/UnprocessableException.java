package br.com.calculatorapi.exception;

public class UnprocessableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnprocessableException(String message) {
		super(message);
	}

}
