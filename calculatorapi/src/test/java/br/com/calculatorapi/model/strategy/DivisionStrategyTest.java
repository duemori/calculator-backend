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

class DivisionStrategyTest {

	private static final String NON_NULL_PARAMS = "Two non-null parameters should be informed.";

	private DivisionStrategy divisionStrategy;

	@BeforeEach
	void setup() {
		this.divisionStrategy = new DivisionStrategy();
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, value = OperationType.class, names = "DIVISION")
	void testIsSupportedWithOperationTypeDifferentFromDivisionShouldReturnFalse(OperationType operationType) {
		assertFalse(this.divisionStrategy.isSupported(operationType),
				"should return false for operation different from DIVISION");
	}

	@Test
	void testIsSupportedWithOperationTypeDivisionShouldReturnTrue() {
		assertTrue(this.divisionStrategy.isSupported(OperationType.DIVISION),
				"should return true for operation DIVISION");
	}

	@ParameterizedTest
	@MethodSource("validateWithInvalidParams")
	void testValidateWithInvalidParamsShouldThrowBadRequestException(BigDecimal[] params, String message) {
		var exception = assertThrows(BadRequestException.class, () -> this.divisionStrategy.validate(params),
				"should throw BadRequestException when params is invalid");

		assertEquals(message, exception.getMessage(), "exception message should describe validation error");
	}

	private static Stream<Arguments> validateWithInvalidParams() {
		return Stream.of(
				Arguments.of(new BigDecimal[] {}, NON_NULL_PARAMS),
				Arguments.of(new BigDecimal[] { BigDecimal.ONE }, NON_NULL_PARAMS),
				Arguments.of(new BigDecimal[] { BigDecimal.ZERO, null }, NON_NULL_PARAMS),
				Arguments.of(new BigDecimal[] { null, BigDecimal.TEN }, NON_NULL_PARAMS),
				Arguments.of(new BigDecimal[] { BigDecimal.TEN, BigDecimal.ZERO }, "Divisor cannot be zero.")
		);
	}

	@Test
	void testValidateWithValidParamsShouldReturnItself() {
		var params = new BigDecimal[] { BigDecimal.ONE, BigDecimal.TEN };
		assertEquals(this.divisionStrategy, this.divisionStrategy.validate(params),
				"should return itself when parameters are valid");
	}

	@ParameterizedTest
	@MethodSource("getResponse")
	void testGetResponseShouldReturnExpectedResult(BigDecimal[] params, String result) {
		assertEquals(result, this.divisionStrategy.getResponse(params), "should return expected result");
	}

	private static Stream<Arguments> getResponse() {
		return Stream.of(
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-4.23), BigDecimal.valueOf(1.83) }, "-2.3115"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(7.1584), BigDecimal.valueOf(-2.045) }, "-3.5004"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-15.7423), BigDecimal.valueOf(-0.0018) }, "8745.7222"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(0.1489), BigDecimal.valueOf(0.0057) }, "26.1228")
		);
	}
}
