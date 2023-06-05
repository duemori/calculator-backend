package br.com.calculatorapi.util;

import java.math.BigDecimal;

import br.com.calculatorapi.config.RandomStringConfig;
import br.com.calculatorapi.model.CreditDebit;
import br.com.calculatorapi.model.Operation;
import br.com.calculatorapi.model.OperationType;
import br.com.calculatorapi.model.Transaction;
import br.com.calculatorapi.model.dto.CreditDTO;
import br.com.calculatorapi.model.dto.SortDirection;
import br.com.calculatorapi.model.dto.TransactionDTO;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;
import br.com.calculatorapi.model.vo.OperationVO;

public final class TestDummies {

	private static final int USER_ID = 1;

	private TestDummies() {
		throw new UnsupportedOperationException();
	}

	public static TransactionDTO getTransactionDTO() {
		var transaction = new TransactionDTO();
		transaction.setOperationId(getOperation().getId());
		transaction.setUserId(USER_ID);
		transaction.setParams(new BigDecimal[] { BigDecimal.ONE, BigDecimal.TEN });
		return transaction;
	}

	public static CreditDTO getCreditDTO() {
		var credit = new CreditDTO();
		credit.setUserId(USER_ID);
		credit.setAmount(BigDecimal.TEN);
		return credit;
	}

	public static Transaction getTransaction() {
		var operation = getOperation();

		var transaction = Transaction.builder()
				.withOperationId(operation.getId())
				.withUserId(USER_ID)
				.withAmount(BigDecimal.ONE)
				.withCreditDebit(CreditDebit.D)
				.withParams("[1,2]")
				.withResponse("3")
				.build();

		transaction.getOperation().setName(operation.getName());

		return transaction;
	}

	public static Transaction getCreditTransaction() {
		return Transaction.builder()
				.withUserId(USER_ID)
				.withAmount(BigDecimal.valueOf(100))
				.withCreditDebit(CreditDebit.C)
				.build();
	}

	public static Operation getOperation() {
		var operation = new Operation();
		operation.setId(1);
		operation.setName(OperationType.ADDITION);
		operation.setDescription("Addition");
		operation.setCost(BigDecimal.valueOf(0.953));
		return operation;
	}

	public static OperationVO getOperationVO() {
		return OperationVO.builder().withId(1).withDescription("Addition").build();
	}

	public static TransactionFilterDTO getTransactionFilterDTO() {
		var transaction = new TransactionFilterDTO();
		transaction.setPage(0);
		transaction.setPerPage(5);
		transaction.setSearch("100");
		transaction.setSortBy("amount");
		transaction.setSortDirection(SortDirection.DESC);
		return transaction;
	}

	public static RandomStringConfig getRandomStringConfig() {
		var config = new RandomStringConfig();
		config.setEnvironment("http://localhost");
		config.setNum(1000);
		config.setLen(4);
		config.setDigits("on");
		config.setUpperalpha("off");
		config.setLoweralpha("off");
		config.setUnique("on");
		config.setFormat("plain");
		config.setRnd("new");
		config.setTimeout(9000);
		return config;
	}
}
