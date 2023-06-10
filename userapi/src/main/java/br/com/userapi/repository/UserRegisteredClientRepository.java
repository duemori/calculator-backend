package br.com.userapi.repository;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

import br.com.userapi.exception.NotFoundException;
import br.com.userapi.model.User;

@Repository
public class UserRegisteredClientRepository implements RegisteredClientRepository {

	private static final String ADMIN_SCOPE = "ADMIN";
	private UserRepository userRepository;

	public UserRegisteredClientRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void save(RegisteredClient client) {
		var user = User.builder().withEmail(client.getClientId()).withPassword(client.getClientSecret()).build();
		this.userRepository.save(user);
	}

	@Override
	public RegisteredClient findById(String id) {
		return findByEmail(id);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		return findByEmail(clientId);
	}

	private RegisteredClient findByEmail(String id) {
		var user = this.userRepository.findByEmailAndActiveTrue(id).orElseThrow(() -> new NotFoundException("Invalid user or password"));
		return RegisteredClient.withId(user.getId().toString())
				.clientId(user.getEmail())
				.clientSecret(user.getPassword())
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope(ADMIN_SCOPE)
				.build();
	}
}
