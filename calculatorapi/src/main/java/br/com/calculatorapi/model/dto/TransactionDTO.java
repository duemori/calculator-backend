package br.com.calculatorapi.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransactionDTO {

	@NotNull(message = "operationId is required")
	@Positive(message = "operationId must be greater than zero")
	private Integer operationId;

	@NotNull(message = "userId is required")
	@Positive(message = "userId must be greater than zero")
	private Integer userId;

	@NotNull(message = "params is required")
	private BigDecimal[] params;

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal[] getParams() {
		return params;
	}

	public void setParams(BigDecimal[] params) {
		this.params = params;
	}
}
