package br.com.userapi.util;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import br.com.userapi.model.User;
import br.com.userapi.model.dto.UserDTO;

public final class TestDummies {

	private static final Integer ID = 1;
	private static final String PASSWORD = "test123";
	private static final String EMAIL = "test@test.com";

	private TestDummies() {
		throw new UnsupportedOperationException();
	}

	public static UserDTO getUserDTO() {
		var user = new UserDTO();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		return user;
	}

	public static User getUser(Integer id) {
		return User.builder().withId(id).withEmail(EMAIL).withPassword(PASSWORD).build();
	}

	public static User getNotActiveUser(Integer id) {
		var user = getUser(id);
		user.setActive(false);
		return user;
	}

	public static RegisteredClient getRegisteredClient() {
		return RegisteredClient
				.withId(ID.toString())
				.clientId(EMAIL)
				.clientSecret(PASSWORD)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.build();
	}
}
