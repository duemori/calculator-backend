package br.com.calculatorapi.model.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.calculatorapi.exception.HttpClientException;
import br.com.calculatorapi.model.OperationType;
import br.com.calculatorapi.service.RandomStringService;
import io.lettuce.core.RedisException;

@ExtendWith(MockitoExtension.class)
class RandomStringStrategyTest {

	private static final String SOME_ERROR = "SOME ERROR";

	@Mock
	private RandomStringService randomStringService;

	private RandomStringStrategy randomStringStrategy;

	@BeforeEach
	void setup() {
		this.randomStringStrategy = new RandomStringStrategy(this.randomStringService);
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, value = OperationType.class, names = "RANDOM_STRING")
	void testIsSupportedWithOperationTypeDifferentFromRandomStringShouldReturnFalse(OperationType operationType) {
		assertFalse(this.randomStringStrategy.isSupported(operationType),
				"should return false for operation different from RANDOM_STRING");
	}

	@Test
	void testIsSupportedWithOperationTypeRandomStringShouldReturnTrue() {
		assertTrue(this.randomStringStrategy.isSupported(OperationType.RANDOM_STRING),
				"should return true for operation RANDOM_STRING");
	}

	@Test
	void testValidateShouldReturnItself() {
		assertEquals(this.randomStringStrategy, this.randomStringStrategy.validate(new BigDecimal[] {}),
				"should return itself when parameters are valid");
	}

	@Test
	void testGetResponseWithExceptionShouldRethrowIt() {
		var throwable = new RedisException(SOME_ERROR);

		when(this.randomStringService.getRandomNumbers()).thenThrow(throwable);

		var exception = assertThrows(HttpClientException.class, () -> this.randomStringStrategy.getResponse(null),
				"should rethrow Exception");

		assertEquals(throwable, exception.getCause(), "exception cause must be equal to the one thrown by the service");
		assertEquals("Error generating random numbers", exception.getMessage(),
				"error message should be about errors generation random numbers");

		verify(this.randomStringService, only()).getRandomNumbers();
	}

	@Test
	void testGetResponseWithSuccessShouldReturnExpectedResult() throws IOException, InterruptedException {
		var number = "10";

		when(this.randomStringService.getRandomNumbers()).thenReturn(List.of(number));

		assertEquals(number, this.randomStringStrategy.getResponse(new BigDecimal[] {}),
				"should return expected result");

		verify(this.randomStringService, only()).getRandomNumbers();
	}
}
