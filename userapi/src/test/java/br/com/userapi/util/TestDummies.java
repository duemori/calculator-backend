package br.com.userapi.util;

import br.com.userapi.model.User;
import br.com.userapi.model.dto.UserDTO;

public final class TestDummies {

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

	public static User getUser() {
		return User.builder().withEmail(EMAIL).withPassword(PASSWORD).build();
	}

	public static User getNotActiveUser() {
		var user = getUser();
		user.setActive(false);
		return user;
	}

}
