package br.com.calculatorapi.model.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Component;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.model.OperationType;

@Component
public class DivisionStrategy implements ICalculator {

	@Override
	public ICalculator validate(BigDecimal[] params) throws BadRequestException {
		if (params.length < 2 || Arrays.stream(params).anyMatch(Objects::isNull)) {
			throw new BadRequestException("Two non-null parameters should be informed.");
		}

		if (params[1].equals(BigDecimal.ZERO)) {
			throw new BadRequestException("Divisor cannot be zero.");
		}

		return this;
	}

	@Override
	public String getResponse(BigDecimal[] params) {
		return params[0].divide(params[1], 4, RoundingMode.HALF_UP).toString();
	}

	@Override
	public boolean isSupported(OperationType operationType) {
		return OperationType.DIVISION == operationType;
	}
}
