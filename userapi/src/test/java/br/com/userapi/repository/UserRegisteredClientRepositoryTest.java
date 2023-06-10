package br.com.userapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import br.com.userapi.exception.NotFoundException;
import br.com.userapi.model.User;
import br.com.userapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class UserRegisteredClientRepositoryTest {

	private static final int ID = 1;

	@Mock
	private UserRepository userRepository;

	@Captor
	private ArgumentCaptor<User> user;

	private UserRegisteredClientRepository userRegisteredClientRepository;

	@BeforeEach
	void setup() {
		this.userRegisteredClientRepository = new UserRegisteredClientRepository(this.userRepository);
	}

	@Test
	void testSaveShouldCallUserSaveWithExpectedUser() {
		var registeredClient = TestDummies.getRegisteredClient();

		when(this.userRepository.save(this.user.capture())).thenReturn(TestDummies.getUser(ID));

		this.userRegisteredClientRepository.save(registeredClient);

		var user = this.user.getValue();

		assertEquals(registeredClient.getClientId(), user.getEmail(), "email must be equal to client id");
		assertEquals(registeredClient.getClientSecret(), user.getPassword(), "password must be equal to client secret");
		assertNotNull(user.getDate(), "date must not be null");
		assertTrue(user.isActive(), "active must be true");

		verify(this.userRepository, only()).save(any(User.class));
	}

	@Test
	void testFindByIdShouldReturnExpectedRegisteredClient() {
		var user = TestDummies.getUser(ID);

		when(this.userRepository.findByEmailAndActiveTrue(anyString())).thenReturn(Optional.of(user));

		var response = this.userRegisteredClientRepository.findById(user.getEmail());

		assertEquals(user.getId().toString(), response.getId(), "id must be equal to user's");
		assertEquals(user.getEmail(), response.getClientId(), "client id must be equal to user's email");
		assertEquals(user.getPassword(), response.getClientSecret(), "client secret must be equal to user's password");
		assertTrue(response.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
				"authentication methods should contain CLIENT_SECRET_BASIC");
		assertTrue(response.getAuthorizationGrantTypes().contains(AuthorizationGrantType.CLIENT_CREDENTIALS),
				"authorization grant types should contain CLIENT_CREDENTIALS");
		assertTrue(response.getScopes().contains("ADMIN"), "scopes should contain ADMIN");

		verify(this.userRepository, only()).findByEmailAndActiveTrue(anyString());
	}

	@Test
	void testFindByClientIdWithInvalidParameterShouldThrowNotFoundException() {
		when(this.userRepository.findByEmailAndActiveTrue(anyString())).thenReturn(Optional.empty());

		var exception = assertThrows(NotFoundException.class, () -> this.userRegisteredClientRepository.findByClientId("INVALID"), "should throw NotFoundException when invalid user email");
		
		assertEquals("Invalid user or password", exception.getMessage(), "message should be about invalid credential");
	}

	@Test
	void testFindByClientIdShouldReturnExpectedRegisteredClient() {
		var user = TestDummies.getUser(ID);

		when(this.userRepository.findByEmailAndActiveTrue(anyString())).thenReturn(Optional.of(user));

		var response = this.userRegisteredClientRepository.findByClientId(user.getEmail());

		assertEquals(user.getId().toString(), response.getId(), "id must be equal to user's");
		assertEquals(user.getEmail(), response.getClientId(), "client id must be equal to user's email");
		assertEquals(user.getPassword(), response.getClientSecret(), "client secret must be equal to user's password");
		assertTrue(response.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
				"authentication methods should contain CLIENT_SECRET_BASIC");
		assertTrue(response.getAuthorizationGrantTypes().contains(AuthorizationGrantType.CLIENT_CREDENTIALS),
				"authorization grant types should contain CLIENT_CREDENTIALS");
		assertTrue(response.getScopes().contains("ADMIN"), "scopes should contain ADMIN");

		verify(this.userRepository, only()).findByEmailAndActiveTrue(anyString());
	}
}
