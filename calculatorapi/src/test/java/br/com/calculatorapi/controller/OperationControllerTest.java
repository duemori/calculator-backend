package br.com.calculatorapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.calculatorapi.service.OperationService;
import br.com.calculatorapi.util.TestDummies;

@WebMvcTest(controllers = OperationController.class)
class OperationControllerTest {

	@MockBean
	private OperationService operationService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateWithSuccessShouldReturnStatusOk() throws Exception {
		var operation = TestDummies.getOperationVO();

		when(this.operationService.findAll()).thenReturn(List.of(operation));

		this.mockMvc.perform(get("/v1/operations"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(operation.getId()))
				.andExpect(jsonPath("$[0].description").value(operation.getDescription()));

		verify(this.operationService, only()).findAll();
	}
}
