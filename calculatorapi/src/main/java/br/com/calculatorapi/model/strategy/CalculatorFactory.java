package br.com.calculatorapi.model.strategy;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.calculatorapi.model.OperationType;

@Component
public class CalculatorFactory {

	private final Collection<ICalculator> calculators;

	public CalculatorFactory(Collection<ICalculator> calculators) {
		this.calculators = calculators;
	}

	public ICalculator getCalculator(OperationType operationType) {
		for (var calculator : calculators) {
			if (calculator.isSupported(operationType)) {
				return calculator;
			}
		}

		return null;
	}
}
