package br.com.calculatorapi.model.strategy;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.exception.HttpClientException;
import br.com.calculatorapi.model.OperationType;
import br.com.calculatorapi.service.RandomStringService;

@Component
public class RandomStringStrategy implements ICalculator {

	private final RandomStringService randomStringService;

	public RandomStringStrategy(RandomStringService randomStringService) {
		this.randomStringService = randomStringService;
	}

	@Override
	public ICalculator validate(BigDecimal[] params) throws BadRequestException {
		return this;
	}

	@Override
	public String getResponse(BigDecimal[] params) {
		try {
			var numbers = this.randomStringService.getRandomNumbers();
			var index = ThreadLocalRandom.current().nextInt(numbers.size()) % numbers.size();
			return numbers.get(index);
		} catch (Exception e) {
			throw new HttpClientException("Error generating random numbers", e);
		}
	}

	@Override
	public boolean isSupported(OperationType operationType) {
		return OperationType.RANDOM_STRING == operationType;
	}
}
