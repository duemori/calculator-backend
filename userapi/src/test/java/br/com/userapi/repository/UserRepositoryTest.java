package br.com.userapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.userapi.model.User;
import br.com.userapi.model.dto.UserFilterDTO;
import br.com.userapi.util.TestDummies;
import br.com.userapi.util.UserSpecifications;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
class TransactionRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private User user1;
	private User user2;
	private List<Integer> ids;

	@BeforeAll
	void setup() {
		this.user1 = this.userRepository.save(TestDummies.getUser());
		this.user2 = this.userRepository.save(TestDummies.getNotActiveUser());

		this.ids = List.of(user1.getId(), user2.getId());
	}

	@Test
	void testExistsByEmailAndActiveTrueShouldReturnExpectedResult() {
		assertFalse(this.userRepository.existsByEmailAndActiveTrue("INVALID"),
				"should return false when no active user found");
		assertTrue(this.userRepository.existsByEmailAndActiveTrue(user1.getEmail()),
				"should return true when active user found");
	}

	@Test
	void testFindAllWithEmptyFilterShouldReturnAllUsersSaved() {
		var response = this.userRepository.findAll(UserSpecifications.createBy(new UserFilterDTO()));

		assertEquals(2, response.size(), "should return both saved users");
		assertTrue(response.stream().map(User::getId).allMatch(ids::contains),
				"ids from response should be equal to those from saved users");
	}

	@Test
	void testFindAllWithFilterShouldReturnJustTransactionSpecified() {
		var filter = new UserFilterDTO();
		filter.setId(user1.getId());
		filter.setEmail(user1.getEmail());
		filter.setActive(user1.isActive());
		filter.setDate(user1.getDate().toLocalDate());

		var response = this.userRepository.findAll(UserSpecifications.createBy(filter));

		assertEquals(1, response.size(), "should return just the user filtered");
		assertEquals(this.user1.getId(), response.get(0).getId(),
				"id from response should be equal to the one filtered");
	}
}
