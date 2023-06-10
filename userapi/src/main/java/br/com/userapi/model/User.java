package br.com.userapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String email;

	private String password;

	private LocalDateTime date;

	private boolean active;

	public User() {	}

	public User(UserBuilder builder) {
		this.id = builder.id;
		this.password = builder.password;
		this.email = builder.email;
		this.date = LocalDateTime.now();
		this.active = true;
	}

	public static UserBuilder builder() {
		return new UserBuilder();
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", date=" + date + ", active=" + active + "]";
	}

	public static class UserBuilder {
		private Integer id;
		private String email;
		private String password;

		private UserBuilder() {	}

		public UserBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public UserBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public UserBuilder withPassword(String password) {
			this.password = password;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}
}
