package br.com.calculatorapi.model.vo;

public class OperationVO {

	private Integer id;

	private String description;

	private Byte params;

	public OperationVO(OperationDTOBuilder builder) {
		this.id = builder.id;
		this.description = builder.description;
		this.params = builder.params;
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

	public Byte getParams() {
		return params;
	}

	public static class OperationDTOBuilder {
		private Integer id;
		private String description;
		private Byte params;

		private OperationDTOBuilder() { }

		public OperationDTOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public OperationDTOBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public OperationDTOBuilder withParams(Byte params) {
			this.params = params;;
			return this;
		}

		public OperationVO build() {
			return new OperationVO(this);
		}
	}
}
