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

class SubtractionStrategyTest {

	private SubtractionStrategy subtractionStrategy;

	@BeforeEach
	void setup() {
		this.subtractionStrategy = new SubtractionStrategy();
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, value = OperationType.class, names = "SUBTRACTION")
	void testIsSupportedWithOperationTypeDifferentFromSubtractionShouldReturnFalse(OperationType operationType) {
		assertFalse(this.subtractionStrategy.isSupported(operationType),
				"should return false for operation different from SUBTRACTION");
	}

	@Test
	void testIsSupportedWithOperationTypeSubtractionShouldReturnTrue() {
		assertTrue(this.subtractionStrategy.isSupported(OperationType.SUBTRACTION),
				"should return true for operation SUBTRACTION");
	}

	@ParameterizedTest
	@MethodSource("validateWithInvalidParams")
	void testValidateWithInvalidParamsShouldThrowBadRequestException(BigDecimal[] params) {
		var exception = assertThrows(BadRequestException.class, () -> this.subtractionStrategy.validate(params),
				"should throw BadRequestException when params is invalid");

		assertEquals("Two non-null parameters should be informed.", exception.getMessage(),
				"exception message should describe validation error");
	}

	private static Stream<Arguments> validateWithInvalidParams() {
		return Stream.of(
				Arguments.of((Object) new BigDecimal[] {}),
				Arguments.of((Object) new BigDecimal[] { BigDecimal.ONE }),
				Arguments.of((Object) new BigDecimal[] { BigDecimal.ZERO, null }),
				Arguments.of((Object) new BigDecimal[] { null, BigDecimal.TEN })
		);
	}

	@Test
	void testValidateWithValidParamsShouldReturnItself() {
		var params = new BigDecimal[] { BigDecimal.ONE, BigDecimal.ZERO };
		assertEquals(this.subtractionStrategy, this.subtractionStrategy.validate(params),
				"should return itself when parameters are valid");
	}

	@ParameterizedTest
	@MethodSource("getResponse")
	void testGetResponseShouldReturnExpectedResult(BigDecimal[] params, String result) {
		assertEquals(result, this.subtractionStrategy.getResponse(params), "should return expected result");
	}

	private static Stream<Arguments> getResponse() {
		return Stream.of(
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-5.41098), BigDecimal.valueOf(7.32785432) }, "-12.73883432"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(2.145), BigDecimal.valueOf(-9.474747) }, "11.619747"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-1.4528), BigDecimal.valueOf(-2.47896) }, "1.02616"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(8.1568), BigDecimal.valueOf(2.51251) }, "5.64429")
		);
	}
}
