package br.com.calculatorapi.util;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.calculatorapi.model.Transaction;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;

public final class TransactionSpecifications {

	private static final String ACTIVE = "active";
	private static final String LIKE_PATTERN = "%%%s%%";
	private static final String DATE = "date";
	private static final String RESPONSE = "response";
	private static final String PARAMS = "params";
	private static final String CREDIT_DEBIT = "creditDebit";
	private static final String AMOUNT = "amount";
	private static final String USER_ID = "userId";
	private static final String OPERATION = "operation";
	private static final String DESCRIPTION = "description";
	private static final String ID = "id";

	private TransactionSpecifications() {
		throw new UnsupportedOperationException();
	}

	public static Specification<Transaction> createBy(TransactionFilterDTO filter) {
		if (!StringUtils.hasText(filter.getSearch())) {
			return isActive();
		}

		var search = String.format(LIKE_PATTERN, filter.getSearch());

		return like(ID, search)
				.or(like(USER_ID, search))
				.or(like(AMOUNT, search))
				.or(like(CREDIT_DEBIT, search))
				.or(like(DATE, search))
				.or(like(PARAMS, search))
				.or(like(RESPONSE, search))
				.or(operationLike(search))
				.and(isActive());
	}

	public static Specification<Transaction> like(String field, String search) {
		return (root, query, builder) -> builder.like(root.get(field).as(String.class), search);
	}

	public static Specification<Transaction> operationLike(String search) {
		return (root, query, builder) -> builder.like(root.join(OPERATION, JoinType.LEFT).get(DESCRIPTION), search);
	}

	public static Specification<Transaction> isActive() {
		return (root, query, builder) -> builder.equal(root.get(ACTIVE), true);
	}
}
