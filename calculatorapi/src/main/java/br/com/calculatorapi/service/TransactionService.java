package br.com.calculatorapi.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.calculatorapi.exception.NotFoundException;
import br.com.calculatorapi.exception.UnprocessableException;
import br.com.calculatorapi.model.CreditDebit;
import br.com.calculatorapi.model.Transaction;
import br.com.calculatorapi.model.dto.CreditDTO;
import br.com.calculatorapi.model.dto.TransactionDTO;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;
import br.com.calculatorapi.model.strategy.CalculatorFactory;
import br.com.calculatorapi.model.vo.TransactionVO;
import br.com.calculatorapi.repository.OperationRepository;
import br.com.calculatorapi.repository.TransactionRepository;
import br.com.calculatorapi.util.TransactionSpecifications;

@Service
public class TransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

	private final OperationRepository operationRepository;
	private final TransactionRepository transactionRepository;
	private final CalculatorFactory calculatorFactory;

	public TransactionService(OperationRepository operationRepository, TransactionRepository transactionRepository,
			CalculatorFactory calculatorFactory) {
		this.operationRepository = operationRepository;
		this.transactionRepository = transactionRepository;
		this.calculatorFactory = calculatorFactory;
	}

	public Integer create(TransactionDTO transaction, Integer userId) throws UnprocessableException {
		var operation = this.operationRepository.findById(transaction.getOperationId());

		if (operation.isEmpty()) {
			throw new UnprocessableException("Invalid operation");
		}

		var transactions = this.transactionRepository.findAllByUserIdAndActiveTrue(userId);
		var balance = transactions.stream()
				.map(t -> t.getAmount().multiply(t.getCreditDebit().getMultiplier()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (operation.get().getCost().compareTo(balance) > 0) {
			throw new UnprocessableException("Balance lower than operation cost");
		}

		var response = this.calculatorFactory.getCalculator(operation.get().getName())
				.validate(transaction.getParams())
				.getResponse(transaction.getParams());

		var newTransaction = this.transactionRepository.save(Transaction.builder()
				.withOperationId(transaction.getOperationId())
				.withUserId(userId)
				.withAmount(operation.get().getCost())
				.withCreditDebit(CreditDebit.D)
				.withParams(Arrays.toString(transaction.getParams()))
				.withResponse(response)
				.build()
		);

		LOGGER.info("Transaction created: {}", newTransaction);

		return newTransaction.getId();
	}

	public Integer create(CreditDTO credit, Integer userId) {
		var newTransaction = this.transactionRepository.save(Transaction.builder()
				.withUserId(userId)
				.withAmount(credit.getAmount())
				.withCreditDebit(CreditDebit.C)
				.build()
		);

		LOGGER.info("Credit transaction created: {}", newTransaction);

		return newTransaction.getId();
	}

	public Page<TransactionVO> findAll(TransactionFilterDTO filter, Integer userId) {
		return this.transactionRepository.findAll(TransactionSpecifications.createBy(filter, userId), getPageable(filter))
				.map(transaction -> TransactionVO.builder()
						.withId(transaction.getId())
						.withOperation(transaction.getOperation() == null ? null : transaction.getOperation().getDescription())
						.withAmount(transaction.getAmount())
						.withCreditDebit(transaction.getCreditDebit())
						.withParams(transaction.getParams())
						.withResponse(transaction.getResponse())
						.withDate(transaction.getDate())
						.build());
	}

	private Pageable getPageable(TransactionFilterDTO filter) {
		if (StringUtils.hasText(filter.getSortBy())) {
			return PageRequest.of(filter.getPage(), filter.getPerPage(), Direction.valueOf(filter.getSortDirection().name()), filter.getSortBy());
		}

		return PageRequest.of(filter.getPage(), filter.getPerPage());
	}

	public void delete(Integer id, Integer userId) throws NotFoundException {
		var transaction = this.transactionRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Transaction not found"));

		LOGGER.info("Transaction id {} inactivated by userId {}", id, userId);

		transaction.setActive(false);

		this.transactionRepository.save(transaction);
	}
}
