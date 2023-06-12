package br.com.calculatorapi.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransactionDTO {

	@NotNull(message = "operationId is required")
	@Positive(message = "operationId must be greater than zero")
	private Integer operationId;

	@NotNull(message = "params is required")
	private BigDecimal[] params;

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public BigDecimal[] getParams() {
		return params;
	}

	public void setParams(BigDecimal[] params) {
		this.params = params;
	}
}
