package br.com.calculatorapi.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "operations")
public class Operation {

	@Id
	private Integer id;

	@Enumerated(EnumType.STRING)
	private OperationType name;

	private String description;

	private BigDecimal cost;

	public Operation() {
	}

	public Operation(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OperationType getName() {
		return name;
	}

	public void setName(OperationType name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Operation [id=" + id + ", name=" + name + ", description=" + description + ", cost=" + cost + "]";
	}
}
