package br.com.userapi.model.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class UserVO {

	private Integer id;

	private String email;

	@JsonFormat(shape = Shape.STRING)
	private LocalDateTime date;

	private boolean active;

	public UserVO(UserVOBuilder builder) {
		this.id = builder.id;
		this.email = builder.email;
		this.active = builder.active;
		this.date = builder.date;
	}

	public static UserVOBuilder builder() {
		return new UserVOBuilder();
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public boolean isActive() {
		return active;
	}

	public static class UserVOBuilder {
		private Integer id;
		private String email;
		private boolean active;
		private LocalDateTime date;

		private UserVOBuilder() { }

		public UserVOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public UserVOBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public UserVOBuilder withActive(boolean active) {
			this.active = active;
			return this;
		}

		public UserVOBuilder withDate(LocalDateTime date) {
			this.date = date;
			return this;
		}

		public UserVO build() {
			return new UserVO(this);
		}
	}
}
