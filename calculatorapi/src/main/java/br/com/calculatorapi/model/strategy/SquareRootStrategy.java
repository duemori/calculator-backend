package br.com.calculatorapi.model.strategy;

import java.math.BigDecimal;
import java.math.MathContext;

import org.springframework.stereotype.Component;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.model.OperationType;

@Component
public class SquareRootStrategy implements ICalculator {

	@Override
	public ICalculator validate(BigDecimal[] params) throws BadRequestException {
		if (params.length < 1 || params[0] == null || params[0].compareTo(BigDecimal.ZERO) < 0) {
			throw new BadRequestException("A non-null parameter higher or equal to zero should be informed.");
		}

		return this;
	}

	@Override
	public String getResponse(BigDecimal[] params) {
		return params[0].sqrt(MathContext.DECIMAL32).toString();
	}

	@Override
	public boolean isSupported(OperationType operationType) {
		return OperationType.SQUARE_ROOT == operationType;
	}
}
