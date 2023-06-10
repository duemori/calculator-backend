package br.com.calculatorapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "operations_id")
	private Operation operation;

	@Column(name = "users_id")
	private Integer userId;

	private BigDecimal amount;

	@Column(name = "credit_debit")
	@Enumerated(EnumType.STRING)
	private CreditDebit creditDebit;

	private String params;

	private String response;

	private LocalDateTime date;

	private boolean active;

	public Transaction() { }

	public Transaction(TransactionBuilder builder) {
		this.operation = builder.operation;
		this.userId = builder.userId;
		this.amount = builder.amount;
		this.creditDebit = builder.creditDebit;
		this.params = builder.params;
		this.response = builder.response;
		this.date = LocalDateTime.now();
		this.active = true;
	}

	public static TransactionBuilder builder() {
		return new TransactionBuilder();
	}

	public Integer getId() {
		return id;
	}

	public Operation getOperation() {
		return operation;
	}

	public Integer getUserId() {
		return userId;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", operation=" + operation + ", userId=" + userId + ", amount=" + amount
				+ ", creditDebit=" + creditDebit + ", params=" + params + ", response=" + response + ", date=" + date
				+ ", active=" + active + "]";
	}

	public static class TransactionBuilder {
		private Operation operation;
		private Integer userId;
		private BigDecimal amount;
		private CreditDebit creditDebit;
		private String params;
		private String response;

		private TransactionBuilder() { }

		public TransactionBuilder withOperationId(Integer operationId) {
			this.operation = new Operation(operationId);
			return this;
		}

		public TransactionBuilder withUserId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public TransactionBuilder withAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public TransactionBuilder withCreditDebit(CreditDebit creditDebit) {
			this.creditDebit = creditDebit;
			return this;
		}

		public TransactionBuilder withParams(String params) {
			this.params = params;
			return this;
		}

		public TransactionBuilder withResponse(String response) {
			this.response = response;
			return this;
		}

		public Transaction build() {
			return new Transaction(this);
		}
	}
}
