package br.com.calculatorapi.model.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.calculatorapi.model.OperationType;

@ExtendWith(MockitoExtension.class)
class CalculatorFactoryTest {

	@Mock
	private ICalculator calculator;

	@Test
	void testGetCalculatorWithoutSupportedCalculatorShouldReturnNull() {
		var calculatorFactory = new CalculatorFactory(List.of());
		assertNull(calculatorFactory.getCalculator(OperationType.ADDITION),
				"should return null when no calculator was found");
	}

	@Test
	void testGetCalculatorWithSupportedCalculatorShouldReturnIt() {
		var calculatorFactory = new CalculatorFactory(List.of(this.calculator, this.calculator));

		when(this.calculator.isSupported(any(OperationType.class))).thenReturn(false).thenReturn(true);

		assertEquals(this.calculator, calculatorFactory.getCalculator(OperationType.ADDITION),
				"should return calculator when supported");
	}
}
