package br.com.userapi.util;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import br.com.userapi.model.User;
import br.com.userapi.model.dto.UserFilterDTO;

public final class UserSpecifications {

	private static final String ACTIVE = "active";
	private static final String LIKE_PATTERN = "%%%s%%";
	private static final String DATE = "date";
	private static final String EMAIL = "email";
	private static final String ID = "id";

	private UserSpecifications() {
		throw new UnsupportedOperationException();
	}

	public static Specification<User> createBy(UserFilterDTO filter) {
		return equalIdIfNotNull(filter.getId())
				.and(likeEmailIfNotNull(filter.getEmail()))
				.and(dateIfNotNull(filter.getDate()))
				.and(isActive(filter.getActive()));
	}

	public static Specification<User> equalIdIfNotNull(Integer id) {
		return (root, query, builder) -> {
			if (id == null) {
				return builder.conjunction();
			}
			return builder.equal(root.get(ID), id);
		};
	}

	public static Specification<User> likeEmailIfNotNull(String email) {
		return (root, query, builder) -> {
			if (email == null) {
				return builder.conjunction();
			}
			return builder.like(root.get(EMAIL), String.format(LIKE_PATTERN, email));
		};
	}

	public static Specification<User> dateIfNotNull(LocalDate date) {
		return (root, query, builder) -> {
			if (date == null) {
				return builder.conjunction();
			}
			return builder.between(root.get(DATE), date.atStartOfDay(), date.plusDays(1).atStartOfDay());
		};
	}

	public static Specification<User> isActive(Boolean active) {
		return (root, query, builder) -> {
			if (active == null) {
				return builder.conjunction();
			}
			return builder.equal(root.get(ACTIVE), active);
		};
	}
}
