package br.com.calculatorapi.model.strategy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Component;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.model.OperationType;

@Component
public class MultiplicationStrategy implements ICalculator {

	@Override
	public ICalculator validate(BigDecimal[] params) throws BadRequestException {
		if (params.length < 2 || Arrays.stream(params).anyMatch(Objects::isNull)) {
			throw new BadRequestException("Two non-null parameters should be informed.");
		}

		return this;
	}

	@Override
	public String getResponse(BigDecimal[] params) {
		return params[0].multiply(params[1]).toString();
	}

	@Override
	public boolean isSupported(OperationType operationType) {
		return OperationType.MULTIPLICATION == operationType;
	}
}
