package br.com.userapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import br.com.userapi.exception.ConflictException;
import br.com.userapi.exception.NotFoundException;
import br.com.userapi.model.User;
import br.com.userapi.model.dto.UserFilterDTO;
import br.com.userapi.repository.UserRepository;
import br.com.userapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private static final int ID = 1;
	private static final boolean ACTIVE = true;

	@Mock
	private UserRepository userRepository;

	private UserService userService;

	@BeforeEach
	void setup() {
		this.userService = new UserService(this.userRepository);
	}

	@Test
	void testCreateWithAlreadyRegisteredEmailShouldThrowConflictException() {
		var user = TestDummies.getUserDTO();

		when(this.userRepository.existsByEmailAndActiveTrue(anyString())).thenReturn(true);

		var exception = assertThrows(ConflictException.class, () -> this.userService.create(user),
				"should throw ConflictException when email already registered");

		assertEquals("User already registered", exception.getMessage(),
				"exception message should be about user already registered");

		verify(this.userRepository, only()).existsByEmailAndActiveTrue(anyString());
	}

	@Test
	void testCreateWithValidUserShouldReturnUserIdFromRepositoryResponse() {
		var user = TestDummies.getUser(ID);

		when(this.userRepository.existsByEmailAndActiveTrue(anyString())).thenReturn(false);
		when(this.userRepository.save(any(User.class))).thenReturn(user);

		var response = this.userService.create(TestDummies.getUserDTO());

		assertEquals(user.getId(), response, "response should be equal to saved user id");

		verify(this.userRepository, times(1)).existsByEmailAndActiveTrue(anyString());
		verify(this.userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(this.userRepository);
	}

	@Test
	void testFindAllShouldReturnTransactionsFromRepositoryQueryResult() {
		var user = TestDummies.getUser(ID);

		when(this.userRepository.findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
				.thenReturn(new PageImpl<User>(List.of(user)));

		var response = this.userService.findAll(new UserFilterDTO());
		var responseUser = response.getContent().get(0);

		assertEquals(1, response.getSize(), "response should have 1 user");
		assertEquals(user.getId(), responseUser.getId(), "id must be the same returned from repository");
		assertEquals(user.getEmail(), responseUser.getEmail(), "email must be the same returned from repository");
		assertEquals(user.getDate(), responseUser.getDate(), "date must be the same returned from repository");
		assertEquals(user.isActive(), responseUser.isActive(), "active must be the same returned from repository");

		verify(this.userRepository, only()).findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class));
	}

	@Test
	void testUpdateStatusWithInvalidIdShouldThrowNotFoundException() {
		when(this.userRepository.findById(anyInt())).thenReturn(Optional.empty());

		var exception = assertThrows(NotFoundException.class, () -> this.userService.updateStatus(ID, ACTIVE),
				"should throw NotFoundException when user does not exist");

		assertEquals("User not found", exception.getMessage(),
				"exception message should be about user not found");

		verify(this.userRepository, only()).findById(anyInt());
	}

	@Test
	void testUpdateStatusToSameStatusShouldJustReturn() {
		var user = TestDummies.getUser(ID);

		when(this.userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		this.userService.updateStatus(user.getId(), user.isActive());

		verify(this.userRepository, only()).findById(anyInt());
	}

	@Test
	void testUpdateStatusFromNotActiveToActiveWithUserAlreadyRegisteredShouldThrowConflictException() {
		var user = TestDummies.getNotActiveUser(ID);

		when(this.userRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(this.userRepository.existsByEmailAndActiveTrue(anyString())).thenReturn(true);

		var exception = assertThrows(ConflictException.class, () -> this.userService.updateStatus(ID, ACTIVE),
				"should throw ConflictException when email already registered");

		assertEquals("User already registered", exception.getMessage(),
				"exception message should be about user already registered");

		verify(this.userRepository, times(1)).findById(anyInt());
		verify(this.userRepository, times(1)).existsByEmailAndActiveTrue(anyString());
		verifyNoMoreInteractions(this.userRepository);
	}

	@Test
	void testUpdateStatusFromNotActiveToActiveWithUserNotRegisteredShouldSaveModelUpdated() {
		var user = TestDummies.getNotActiveUser(ID);

		when(this.userRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(this.userRepository.existsByEmailAndActiveTrue(anyString())).thenReturn(false);
		when(this.userRepository.save(any(User.class))).thenReturn(user);

		this.userService.updateStatus(user.getId(), ACTIVE);

		verify(this.userRepository, times(1)).findById(anyInt());
		verify(this.userRepository, times(1)).existsByEmailAndActiveTrue(anyString());
		verify(this.userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(this.userRepository);
	}

	@Test
	void testUpdateStatusToNotActiveShouldSaveModelUpdated() {
		var user = TestDummies.getUser(ID);

		when(this.userRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(this.userRepository.save(any(User.class))).thenReturn(user);

		this.userService.updateStatus(user.getId(), false);

		verify(this.userRepository, times(1)).findById(anyInt());
		verify(this.userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(this.userRepository);
	}
}
