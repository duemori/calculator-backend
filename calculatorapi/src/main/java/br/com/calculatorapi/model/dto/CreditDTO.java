package br.com.calculatorapi.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreditDTO {

	@NotNull(message = "userId is required")
	@Positive(message = "userId must be greater than zero")
	private Integer userId;

	@NotNull(message = "amount is required")
	@Positive(message = "amount must be greater than zero")
	private BigDecimal amount;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
