package br.com.userapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.userapi.exception.ConflictException;
import br.com.userapi.exception.NotFoundException;
import br.com.userapi.model.User;
import br.com.userapi.model.dto.UserDTO;
import br.com.userapi.model.dto.UserFilterDTO;
import br.com.userapi.model.vo.UserVO;
import br.com.userapi.repository.UserRepository;
import br.com.userapi.util.UserSpecifications;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Integer create(UserDTO user) throws ConflictException {
		validateEmailAlreadyRegistered(user.getEmail());

		var newUser = this.userRepository
				.save(User.builder().withEmail(user.getEmail()).withPassword(user.getPassword()).build());

		LOGGER.info("User created: {}", newUser);

		return newUser.getId();
	}

	private void validateEmailAlreadyRegistered(String email) {
		if (this.userRepository.existsByEmailAndActiveTrue(email)) {
			throw new ConflictException("User already registered");
		}
	}

	public Page<UserVO> findAll(UserFilterDTO filter) {
		var pageable = PageRequest.of(filter.getPage(), filter.getPerPage());

		return this.userRepository.findAll(UserSpecifications.createBy(filter), pageable)
				.map(user -> UserVO.builder()
						.withId(user.getId())
						.withEmail(user.getEmail())
						.withActive(user.isActive())
						.withDate(user.getDate())
						.build());
	}

	public void updateStatus(Integer id, Boolean active) throws NotFoundException, ConflictException {
		User user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

		if (active.equals(user.isActive())) {
			return;
		}

		if (Boolean.TRUE.equals(active)) {
			validateEmailAlreadyRegistered(user.getEmail());
		}

		user.setActive(active);

		this.userRepository.save(user);

		LOGGER.info("User id {} updated status to {}", id, active);
	}
}
