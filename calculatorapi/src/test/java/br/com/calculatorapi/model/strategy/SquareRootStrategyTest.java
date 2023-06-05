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

class SquareRootStrategyTest {

	private SquareRootStrategy squareRootStrategy;

	@BeforeEach
	void setup() {
		this.squareRootStrategy = new SquareRootStrategy();
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, value = OperationType.class, names = "SQUARE_ROOT")
	void testIsSupportedWithOperationTypeDifferentFromSquareRootShouldReturnFalse(OperationType operationType) {
		assertFalse(this.squareRootStrategy.isSupported(operationType),
				"should return false for operation different from SQUARE_ROOT");
	}

	@Test
	void testIsSupportedWithOperationTypeSquareRootShouldReturnTrue() {
		assertTrue(this.squareRootStrategy.isSupported(OperationType.SQUARE_ROOT),
				"should return true for operation SQUARE_ROOT");
	}

	@ParameterizedTest
	@MethodSource("validateWithInvalidParams")
	void testValidateWithInvalidParamsShouldThrowBadRequestException(BigDecimal[] params) {
		var exception = assertThrows(BadRequestException.class, () -> this.squareRootStrategy.validate(params),
				"should throw BadRequestException when params is invalid");

		assertEquals("A non-null parameter higher or equal to zero should be informed.", exception.getMessage(),
				"exception message should describe validation error");
	}

	private static Stream<Arguments> validateWithInvalidParams() {
		return Stream.of(
				Arguments.of((Object)new BigDecimal[] {}),
				Arguments.of((Object)new BigDecimal[] { null }),
				Arguments.of((Object)new BigDecimal[] { BigDecimal.valueOf(-1) })
		);
	}

	@Test
	void testValidateWithValidParamsShouldReturnItself() {
		var params = new BigDecimal[] { BigDecimal.ONE };
		assertEquals(this.squareRootStrategy, this.squareRootStrategy.validate(params),
				"should return itself when parameters are valid");
	}

	@ParameterizedTest
	@MethodSource("getResponse")
	void testGetResponseShouldReturnExpectedResult(BigDecimal[] params, String result) {
		assertEquals(result, this.squareRootStrategy.getResponse(params), "should return expected result");
	}

	private static Stream<Arguments> getResponse() {
		return Stream.of(
				Arguments.of(new BigDecimal[] { BigDecimal.ZERO }, "0"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(1.54789215) }, "1.244143"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(144) }, "12")
		);
	}
}
