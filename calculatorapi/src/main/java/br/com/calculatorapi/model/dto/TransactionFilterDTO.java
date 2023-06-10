package br.com.calculatorapi.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class TransactionFilterDTO {
	private String search;

	private String sortBy;

	private SortDirection sortDirection;

	@PositiveOrZero(message = "page have to be greater or equal zero")
	private Integer page;

	@Positive(message = "perPage have to be greater than zero")
	private Integer perPage;

	public TransactionFilterDTO() {
		page = 0;
		perPage = 5;
		sortDirection = SortDirection.ASC;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public SortDirection getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
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
