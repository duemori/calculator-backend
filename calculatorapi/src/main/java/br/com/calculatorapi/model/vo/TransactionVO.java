package br.com.calculatorapi.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.calculatorapi.model.CreditDebit;

public class TransactionVO {

	private Integer id;

	private String operation;

	private BigDecimal amount;

	private CreditDebit creditDebit;

	private String params;

	private String response;

	@JsonFormat(shape = Shape.STRING)
	private LocalDateTime date;

	public TransactionVO(TransactionVOBuilder builder) {
		this.id = builder.id;
		this.operation = builder.operation;
		this.amount = builder.amount;
		this.creditDebit = builder.creditDebit;
		this.params = builder.params;
		this.response = builder.response;
		this.date = builder.date;
	}

	public static TransactionVOBuilder builder() {
		return new TransactionVOBuilder();
	}

	public Integer getId() {
		return id;
	}

	public String getOperation() {
		return operation;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public CreditDebit getCreditDebit() {
		return creditDebit;
	}

	public String getParams() {
		return params;
	}

	public String getResponse() {
		return response;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public static class TransactionVOBuilder {
		private Integer id;
		private String operation;
		private BigDecimal amount;
		private CreditDebit creditDebit;
		private String params;
		private String response;
		private LocalDateTime date;

		private TransactionVOBuilder() { }

		public TransactionVOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public TransactionVOBuilder withOperation(String operation) {
			this.operation = operation;
			return this;
		}

		public TransactionVOBuilder withAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public TransactionVOBuilder withCreditDebit(CreditDebit creditDebit) {
			this.creditDebit = creditDebit;
			return this;
		}

		public TransactionVOBuilder withParams(String params) {
			this.params = params;
			return this;
		}

		public TransactionVOBuilder withResponse(String response) {
			this.response = response;
			return this;
		}

		public TransactionVOBuilder withDate(LocalDateTime date) {
			this.date = date;
			return this;
		}

		public TransactionVO build() {
			return new TransactionVO(this);
		}
	}
}
