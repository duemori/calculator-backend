package br.com.calculatorapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.calculatorapi.repository.OperationRepository;
import br.com.calculatorapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

	@Mock
	private OperationRepository operationRepository;

	private OperationService operationService;

	@BeforeEach
	void setup() {
		this.operationService = new OperationService(this.operationRepository);
	}

	@Test
	void testFindAllWithSuccess() {
		var operation = TestDummies.getOperation();

		when(this.operationRepository.findAll()).thenReturn(List.of(operation));

		var response = this.operationService.findAll();

		assertEquals(1, response.size(), "must have only one element");
		assertEquals(operation.getId(), response.get(0).getId(), "id must be equal to operation´s");
		assertEquals(operation.getDescription(), response.get(0).getDescription(),
				"description must be equal to operation´s");
		assertEquals(operation.getParams(), response.get(0).getParams(), "params must be equal to operation´s");
	}
}
