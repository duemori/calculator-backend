package br.com.calculatorapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.calculatorapi.exception.BadRequestException;
import br.com.calculatorapi.exception.HttpClientException;
import br.com.calculatorapi.exception.NotFoundException;
import br.com.calculatorapi.exception.UnprocessableException;
import br.com.calculatorapi.model.dto.CreditDTO;
import br.com.calculatorapi.model.dto.TransactionDTO;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;
import br.com.calculatorapi.service.TransactionService;
import br.com.calculatorapi.util.TestDummies;

@WithMockUser
@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

	private static final JwtRequestPostProcessor JWT = jwt().jwt(t -> t.claim("uid", "1"));

	@MockBean
	private TransactionService transactionService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateWithoutRequiredFieldsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(post("/v1/transactions").with(csrf()).with(JWT).contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("operationId is required")))
				.andExpect(content().string(containsString("params is required")));

		verifyNoInteractions(this.transactionService);
	}

	@ParameterizedTest
	@MethodSource("exceptionThrown")
	void testCreateWithExceptionThrownShouldReturnExpectedStatus(Throwable throwable, HttpStatus status, String errorMessage) throws Exception {
		var body = new ObjectMapper().writeValueAsString(TestDummies.getTransactionDTO());

		when(this.transactionService.create(any(TransactionDTO.class), anyInt())).thenThrow(throwable);

		this.mockMvc.perform(post("/v1/transactions").with(csrf()).with(JWT).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().is(status.value()))
				.andExpect(content().string(errorMessage));

		verify(this.transactionService, only()).create(any(TransactionDTO.class), anyInt());
	}

	private static Stream<Arguments> exceptionThrown() {
		var errorMessage = "SOME ERROR";

		return Stream.of(
				Arguments.of(new UnprocessableException(errorMessage), HttpStatus.UNPROCESSABLE_ENTITY, errorMessage),
				Arguments.of(new BadRequestException(errorMessage), HttpStatus.BAD_REQUEST, errorMessage),
				Arguments.of(new NotFoundException(errorMessage), HttpStatus.NOT_FOUND, errorMessage),
				Arguments.of(new HttpClientException(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")
		);
	}

	@Test
	void testCreateWithValidBodyShouldReturnStatusCreated() throws Exception {
		var id = 1;
		var body = new ObjectMapper().writeValueAsString(TestDummies.getTransactionDTO());

		when(this.transactionService.create(any(TransactionDTO.class), anyInt())).thenReturn(id);

		this.mockMvc.perform(post("/v1/transactions").with(csrf()).with(JWT).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(content().string(""))
				.andExpect(header().string("location", "/v1/transactions/" + id));

		verify(this.transactionService, only()).create(any(TransactionDTO.class), anyInt());
	}

	@Test
	void testCreditWithoutRequiredFieldsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(post("/v1/transactions/credits").with(csrf()).with(JWT).contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("amount is required")));

		verifyNoInteractions(this.transactionService);
	}

	@Test
	void testCreditWithValidBodyShouldReturnStatusCreated() throws Exception {
		var id = 1;
		var body = new ObjectMapper().writeValueAsString(TestDummies.getCreditDTO());

		when(this.transactionService.create(any(CreditDTO.class), anyInt())).thenReturn(id);

		this.mockMvc.perform(post("/v1/transactions/credits").with(csrf()).with(JWT).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(content().string(""))
				.andExpect(header().string("location", "/v1/transactions/" + id));

		verify(this.transactionService, only()).create(any(CreditDTO.class), anyInt());
	}

	@Test
	void testFindAllWithInvalidParamsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(get("/v1/transactions?page=-1&perPage=0"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("page have to be greater or equal zero")))
				.andExpect(content().string(containsString("perPage have to be greater than zero")));

		verifyNoInteractions(this.transactionService);
	}

	@Test
	void testFindAllWithValidParamsShouldReturnStatusOk() throws Exception {
		when(this.transactionService.findAll(any(TransactionFilterDTO.class), anyInt())).thenReturn(Page.empty());

		this.mockMvc.perform(get("/v1/transactions?page=1&perPage=10").with(JWT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(hasSize(0)))
				.andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.totalElements").value(0));

		verify(this.transactionService, only()).findAll(any(TransactionFilterDTO.class), anyInt());
	}

	@Test
	void testDeleteWithValidIdShouldReturnStatusNoContent() throws Exception {
		doNothing().when(this.transactionService).delete(anyInt(), anyInt());

		this.mockMvc.perform(delete("/v1/transactions/1").with(csrf()).with(JWT))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));

		verify(this.transactionService, only()).delete(anyInt(), anyInt());
	}
}
