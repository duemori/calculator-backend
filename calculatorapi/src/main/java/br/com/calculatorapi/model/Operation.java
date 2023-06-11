package br.com.calculatorapi.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "operations")
public class Operation {

	@Id
	private Integer id;

	@Enumerated(EnumType.STRING)
	private OperationType name;

	private String description;

	private BigDecimal cost;

	private Byte params;

	public Operation() { }

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

	public Byte getParams() {
		return params;
	}

	public void setParams(Byte params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "Operation [id=" + id + ", name=" + name + ", description=" + description + ", cost=" + cost
				+ ", params=" + params + "]";
	}
}
