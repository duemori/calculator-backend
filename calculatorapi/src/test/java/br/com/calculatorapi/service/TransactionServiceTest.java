package br.com.calculatorapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import br.com.calculatorapi.exception.NotFoundException;
import br.com.calculatorapi.exception.UnprocessableException;
import br.com.calculatorapi.model.CreditDebit;
import br.com.calculatorapi.model.OperationType;
import br.com.calculatorapi.model.Transaction;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;
import br.com.calculatorapi.model.strategy.CalculatorFactory;
import br.com.calculatorapi.model.strategy.ICalculator;
import br.com.calculatorapi.model.vo.TransactionVO;
import br.com.calculatorapi.repository.OperationRepository;
import br.com.calculatorapi.repository.TransactionRepository;
import br.com.calculatorapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private OperationRepository operationRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private CalculatorFactory calculatorFactory;

	@Mock
	private ICalculator calculator;

	private TransactionService transactionService;

	@BeforeEach
	void setup() {
		this.transactionService = new TransactionService(this.operationRepository, this.transactionRepository,
				this.calculatorFactory);
	}

	@Test
	void testCreateWithInvalidOperationIdShouldThrowUnprocessableException() {
		var transaction = TestDummies.getTransactionDTO();

		when(this.operationRepository.findById(anyInt())).thenReturn(Optional.empty());

		var exception = assertThrows(UnprocessableException.class, () -> this.transactionService.create(transaction),
				"should throw UnprocessableException when operation does not exist");

		assertEquals("Invalid operation", exception.getMessage(),
				"exception message should be about invalid operation");

		verify(this.operationRepository, only()).findById(anyInt());
		verifyNoInteractions(this.transactionRepository);
	}

	@Test
	void testCreateWithInsufficientBalanceShouldThrowUnprocessableException() {
		var transaction = TestDummies.getTransactionDTO();

		when(this.operationRepository.findById(anyInt())).thenReturn(Optional.of(TestDummies.getOperation()));
		when(this.transactionRepository.findAllByUserIdAndActiveTrue(anyInt())).thenReturn(List.of());

		var exception = assertThrows(UnprocessableException.class, () -> this.transactionService.create(transaction),
				"should throw UnprocessableException when balance is lower then transaction cost");

		assertEquals("Balance lower than operation cost", exception.getMessage(),
				"exception message should be about balance lower than operation cost");

		verify(this.operationRepository, only()).findById(anyInt());
		verify(this.transactionRepository, only()).findAllByUserIdAndActiveTrue(anyInt());
	}

	@Test
	void testCreateWithValidTransactionShouldReturnTransactionIdFromRepositoryResponse() {
		var transaction = TestDummies.getTransaction();

		when(this.operationRepository.findById(anyInt())).thenReturn(Optional.of(TestDummies.getOperation()));
		when(this.transactionRepository.findAllByUserIdAndActiveTrue(anyInt()))
				.thenReturn(List.of(TestDummies.getCreditTransaction()));
		when(this.calculatorFactory.getCalculator(any(OperationType.class))).thenReturn(this.calculator);
		when(this.calculator.validate(any(BigDecimal[].class))).thenReturn(this.calculator);
		when(this.calculator.getResponse(any(BigDecimal[].class))).thenReturn("99.047");
		when(this.transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		var response = this.transactionService.create(TestDummies.getTransactionDTO());

		assertEquals(transaction.getId(), response, "response should be equal to saved transaction id");

		verify(this.operationRepository, only()).findById(anyInt());
		verify(this.transactionRepository, times(1)).findAllByUserIdAndActiveTrue(anyInt());
		verify(this.calculatorFactory, only()).getCalculator(any(OperationType.class));
		verify(this.transactionRepository, times(1)).save(any(Transaction.class));
		verifyNoMoreInteractions(this.transactionRepository);
	}

	@Test
	void testCreateWithValidCreditTransactionShouldReturnTransactionIdFromRepositoryResponse() {
		var transaction = TestDummies.getTransaction();

		when(this.transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		var response = this.transactionService.create(TestDummies.getCreditDTO());

		assertEquals(transaction.getId(), response, "response should be equal to saved transaction id");

		verify(this.transactionRepository, only()).save(any(Transaction.class));
	}

	@ParameterizedTest
	@MethodSource("findAllFilters")
	void testFindAllShouldReturnTransactionsFromRepositoryQueryResult(TransactionFilterDTO filter) {
		var debit = TestDummies.getTransaction();
		var credit = TestDummies.getCreditTransaction();

		when(this.transactionRepository.findAll(ArgumentMatchers.<Specification<Transaction>>any(),
				any(Pageable.class))).thenReturn(new PageImpl<Transaction>(List.of(debit, credit)));

		var response = this.transactionService.findAll(filter);

		TransactionVO debitResponse = response.stream().filter(r -> r.getCreditDebit() == CreditDebit.D).findFirst()
				.get();

		assertEquals(2, response.getSize(), "response should have 2 transactions");
		assertEquals(debit.getId(), debitResponse.getId(), "id must be the same returned from repository");
		assertEquals(debit.getOperation().getDescription(), debitResponse.getOperation(),
				"operation must be the same returned from repository");
		assertEquals(debit.getUserId(), debitResponse.getUserId(), "userId must be the same returned from repository");
		assertEquals(debit.getAmount(), debitResponse.getAmount(), "amount must be the same returned from repository");
		assertEquals(debit.getCreditDebit(), debitResponse.getCreditDebit(),
				"creditDebit must be the same returned from repository");
		assertEquals(debit.getParams(), debitResponse.getParams(), "params must be the same returned from repository");
		assertEquals(debit.getResponse(), debitResponse.getResponse(),
				"response must be the same returned from repository");
		assertEquals(debit.getDate(), debitResponse.getDate(), "date must be the same returned from repository");

		verify(this.transactionRepository, only()).findAll(ArgumentMatchers.<Specification<Transaction>>any(),
				any(Pageable.class));
	}

	private static List<TransactionFilterDTO> findAllFilters() {
		return List.of(new TransactionFilterDTO(), TestDummies.getTransactionFilterDTO());
	}

	@Test
	void testDeleteWithInvalidIdShouldThrowNotFoundException() {
		when(this.transactionRepository.findById(anyInt())).thenReturn(Optional.empty());

		var exception = assertThrows(NotFoundException.class, () -> this.transactionService.delete(1),
				"should throw NotFoundException when transaction does not exist");

		assertEquals("Transaction not found", exception.getMessage(),
				"exception message should be about transaction not found");

		verify(this.transactionRepository, only()).findById(anyInt());
	}

	@Test
	void testDeleteWithValidIdShouldSaveModelUpdated() {
		var transaction = TestDummies.getTransaction();

		when(this.transactionRepository.findById(anyInt())).thenReturn(Optional.of(transaction));
		when(this.transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		this.transactionService.delete(1);

		verify(this.transactionRepository, times(1)).findById(anyInt());
		verify(this.transactionRepository, times(1)).findById(anyInt());
		verifyNoMoreInteractions(this.transactionRepository);
	}
}
