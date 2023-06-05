package br.com.calculatorapi.model.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.model.OperationType;

class MultiplicationStrategyTest {

	private MultiplicationStrategy multiplicationStrategy;

	@BeforeEach
	void setup() {
		this.multiplicationStrategy = new MultiplicationStrategy();
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, value = OperationType.class, names = "MULTIPLICATION")
	void testIsSupportedWithOperationTypeDifferentFromMultiplicationShouldReturnFalse(OperationType operationType) {
		assertFalse(this.multiplicationStrategy.isSupported(operationType),
				"should return false for operation different from MULTIPLICATION");
	}

	@Test
	void testIsSupportedWithOperationTypeMultiplicationShouldReturnTrue() {
		assertTrue(this.multiplicationStrategy.isSupported(OperationType.MULTIPLICATION),
				"should return true for operation MULTIPLICATION");
	}

	@ParameterizedTest
	@MethodSource("validateWithInvalidParams")
	void testValidateWithInvalidParamsShouldThrowBadRequestException(BigDecimal[] params) {
		var exception = assertThrows(BadRequestException.class, () -> this.multiplicationStrategy.validate(params),
				"should throw BadRequestException when params is invalid");

		assertEquals("Two non-null parameters should be informed.", exception.getMessage(),
				"exception message should describe validation error");
	}

	private static Stream<Arguments> validateWithInvalidParams() {
		return Stream.of(
				Arguments.of((Object)new BigDecimal[] {}),
				Arguments.of((Object)new BigDecimal[] { BigDecimal.ONE }),
				Arguments.of((Object)new BigDecimal[] { BigDecimal.ZERO, null }),
				Arguments.of((Object)new BigDecimal[] { null, BigDecimal.TEN })
		);
	}

	@Test
	void testValidateWithValidParamsShouldReturnItself() {
		var params = new BigDecimal[] { BigDecimal.ONE, BigDecimal.TEN };
		assertEquals(this.multiplicationStrategy, this.multiplicationStrategy.validate(params),
				"should return itself when parameters are valid");
	}

	@ParameterizedTest
	@MethodSource("getResponse")
	void testGetResponseShouldReturnExpectedResult(BigDecimal[] params, String result) {
		assertEquals(result, this.multiplicationStrategy.getResponse(params), "should return expected result");
	}

	private static Stream<Arguments> getResponse() {
		return Stream.of(
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-1.457), BigDecimal.valueOf(0.5479) }, "-0.7982903"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(8.544), BigDecimal.valueOf(-5.3) }, "-45.2832"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-12.32578), BigDecimal.valueOf(-9.214) }, "113.56973692"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(0.024), BigDecimal.valueOf(0.0348) }, "0.0008352")
		);
	}
}
