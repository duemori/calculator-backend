package br.com.calculatorapi.exception;

public class HttpClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HttpClientException(String message) {
		super(message);
	}

	public HttpClientException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
