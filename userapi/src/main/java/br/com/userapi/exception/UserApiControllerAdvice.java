package br.com.userapi.exception;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserApiControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserApiControllerAdvice.class);

	@ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
	public ResponseEntity<String> handleMethodArgumentNotValidException(BindException exception) {
		var errors = exception.getFieldErrors().stream().map(e -> e.getDefaultMessage())
				.collect(Collectors.joining(","));

		LOGGER.error("Invalid method arguments: {}", errors);

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
		var error = exception.getMessage();

		LOGGER.error("Not found: {}", error);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflictException(ConflictException exception) {
		var errorMessage = exception.getMessage();

		LOGGER.error("Conflict: {}", errorMessage);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception exception) {
		LOGGER.error("Internal server error: {}", exception.getMessage());

		return ResponseEntity.internalServerError().body("Internal server error");
	}
}
