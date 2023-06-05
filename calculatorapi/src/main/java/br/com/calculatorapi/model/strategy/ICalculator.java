package br.com.calculatorapi.model.strategy;

import java.math.BigDecimal;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.model.OperationType;

public interface ICalculator {
	ICalculator validate(BigDecimal[] params) throws BadRequestException;

	String getResponse(BigDecimal[] params);

	boolean isSupported(OperationType operationType);
}
