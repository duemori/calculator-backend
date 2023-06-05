package br.com.calculatorapi.model.vo;

public class OperationVO {

	private Integer id;

	private String description;

	public OperationVO(OperationDTOBuilder builder) {
		this.id = builder.id;
		this.description = builder.description;
	}

	public static OperationDTOBuilder builder() {
		return new OperationDTOBuilder();
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public static class OperationDTOBuilder {
		private Integer id;
		private String description;

		private OperationDTOBuilder() {
		}

		public OperationDTOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public OperationDTOBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public OperationVO build() {
			return new OperationVO(this);
		}
	}
}
