package br.com.calculatorapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.calculatorapi.model.Transaction;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;
import br.com.calculatorapi.util.TestDummies;
import br.com.calculatorapi.util.TransactionSpecifications;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
class TransactionRepositoryTest {

	@Autowired
	private OperationRepository operationRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private Transaction transaction1;
	private Transaction transaction2;
	private List<Integer> ids;

	@BeforeAll
	void setup() {
		var inactiveTransaction = TestDummies.getTransaction();
		inactiveTransaction.setActive(false);

		this.operationRepository.save(TestDummies.getOperation());
		this.transaction1 = this.transactionRepository.save(TestDummies.getTransaction());
		this.transaction2 = this.transactionRepository.save(TestDummies.getCreditTransaction());
		this.transactionRepository.save(inactiveTransaction);

		this.ids = List.of(transaction1.getId(), transaction2.getId());
	}

	@Test
	void testFindAllByUserIdAndActiveShouldReturnAllTransactionsSavedWithTheSameUserId() {
		var response = this.transactionRepository.findAllByUserIdAndActiveTrue(this.transaction1.getUserId());

		assertEquals(2, response.size(), "should return both transactions saved with the same userId and active");
		assertTrue(response.stream().map(Transaction::getId).allMatch(ids::contains),
				"should return both transactions saved and active");
		assertTrue(response.stream().allMatch(Transaction::isActive), "should return just active transactions");
	}

	@Test
	void testFindAllWithEmptyFilterShouldReturnAllActiveTransactionsSaved() {
		var response = this.transactionRepository
				.findAll(TransactionSpecifications.createBy(new TransactionFilterDTO()));

		assertEquals(2, response.size(), "should return both transactions saved and active");
		assertTrue(response.stream().map(Transaction::getId).allMatch(ids::contains),
				"should return both transactions saved and active");
		assertTrue(response.stream().allMatch(Transaction::isActive), "should return just active transactions");
	}

	@Test
	void testFindAllWithFilterShouldReturnJustTransactionSpecified() {
		var filter = new TransactionFilterDTO();
		filter.setSearch(this.transaction2.getCreditDebit().name());

		var response = this.transactionRepository.findAll(TransactionSpecifications.createBy(filter));

		assertEquals(1, response.size(), "should return just the transaction filtered");
		assertEquals(this.transaction2.getId(), response.get(0).getId(), "should return the transaction filtered");
		assertTrue(response.get(0).isActive(), "should be active");
	}
}
