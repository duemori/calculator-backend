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

class AdditionStrategyTest {

	private AdditionStrategy additionStrategy;

	@BeforeEach
	void setup() {
		this.additionStrategy = new AdditionStrategy();
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, value = OperationType.class, names = "ADDITION")
	void testIsSupportedWithOperationTypeDifferentFromAdditionShouldReturnFalse(OperationType operationType) {
		assertFalse(this.additionStrategy.isSupported(operationType),
				"should return false for operation different from ADDITION");
	}

	@Test
	void testIsSupportedWithOperationTypeAdditionShouldReturnTrue() {
		assertTrue(this.additionStrategy.isSupported(OperationType.ADDITION),
				"should return true for operation ADDITION");
	}

	@ParameterizedTest
	@MethodSource("validateWithInvalidParams")
	void testValidateWithInvalidParamsShouldThrowBadRequestException(BigDecimal[] params) {
		var exception = assertThrows(BadRequestException.class, () -> this.additionStrategy.validate(params),
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
		assertEquals(this.additionStrategy, this.additionStrategy.validate(params),
				"should return itself when parameters are valid");
	}

	@ParameterizedTest
	@MethodSource("getResponse")
	void testGetResponseShouldReturnExpectedResult(BigDecimal[] params, String result) {
		assertEquals(result, this.additionStrategy.getResponse(params), "should return expected result");
	}

	private static Stream<Arguments> getResponse() {
		return Stream.of(
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-1.55), BigDecimal.valueOf(3.98) }, "2.43"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(5.72), BigDecimal.valueOf(-6.07) }, "-0.35"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(-8.13), BigDecimal.valueOf(-0.87) }, "-9.00"),
				Arguments.of(new BigDecimal[] { BigDecimal.valueOf(0.854), BigDecimal.valueOf(1.7547) }, "2.6087")
		);
	}
}
