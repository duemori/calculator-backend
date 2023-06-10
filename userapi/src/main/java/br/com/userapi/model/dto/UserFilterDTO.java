package br.com.userapi.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class UserFilterDTO {
	private Integer id;

	private String email;

	private Boolean active;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate date;

	@PositiveOrZero(message = "page have to be greater or equal zero")
	private Integer page;

	@Positive(message = "perPage have to be greater than zero")
	private Integer perPage;

	public UserFilterDTO() {
		page = 0;
		perPage = 5;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPerPage() {
		return perPage;
	}

	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}
}
