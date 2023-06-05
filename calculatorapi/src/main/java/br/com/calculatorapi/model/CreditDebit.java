package br.com.calculatorapi.model;

import java.math.BigDecimal;

public enum CreditDebit {
	C(BigDecimal.valueOf(1.0)), D(BigDecimal.valueOf(-1.0));

	private final BigDecimal multiplier;

	private CreditDebit(BigDecimal multiplier) {
		this.multiplier = multiplier;
	}

	public BigDecimal getMultiplier() {
		return multiplier;
	}
}
