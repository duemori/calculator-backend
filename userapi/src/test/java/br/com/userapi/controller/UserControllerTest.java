package br.com.userapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.userapi.exception.ConflictException;
import br.com.userapi.exception.NotFoundException;
import br.com.userapi.model.dto.UserDTO;
import br.com.userapi.model.dto.UserFilterDTO;
import br.com.userapi.service.UserService;
import br.com.userapi.util.TestDummies;

@WithMockUser
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateWithoutRequiredFieldsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(post("/v1/users").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("email is required")))
				.andExpect(content().string(containsString("password is required")));

		verifyNoInteractions(this.userService);
	}

	@Test
	void testCreateWithConflictExceptionShouldReturnStatusConflict() throws Exception {
		var errorMessage = "SOME ERROR";
		var body = new ObjectMapper().writeValueAsString(TestDummies.getUserDTO());

		when(this.userService.create(any(UserDTO.class))).thenThrow(new ConflictException(errorMessage));

		this.mockMvc.perform(post("/v1/users").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isConflict())
				.andExpect(content().string(errorMessage));

		verify(this.userService, only()).create(any(UserDTO.class));
	}

	@Test
	void testCreateWithExceptionShouldReturnStatusInternalServerError() throws Exception {
		var body = new ObjectMapper().writeValueAsString(TestDummies.getUserDTO());

		when(this.userService.create(any(UserDTO.class))).thenThrow(new RuntimeException());

		this.mockMvc.perform(post("/v1/users").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Internal server error"));

		verify(this.userService, only()).create(any(UserDTO.class));
	}

	@Test
	void testCreateWithValidBodyShouldReturnStatusCreated() throws Exception {
		var id = 1;
		var body = new ObjectMapper().writeValueAsString(TestDummies.getUserDTO());

		when(this.userService.create(any(UserDTO.class))).thenReturn(id);

		this.mockMvc.perform(post("/v1/users").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(content().string(""))
				.andExpect(header().string("location", "/v1/users/" + id));

		verify(this.userService, only()).create(any(UserDTO.class));
	}

	@Test
	void testFindAllWithInvalidParamsShouldReturnStatusBadRequest() throws Exception {
		this.mockMvc.perform(get("/v1/users?page=-1&perPage=0"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("page have to be greater or equal zero")))
				.andExpect(content().string(containsString("perPage have to be greater than zero")));

		verifyNoInteractions(this.userService);
	}

	@Test
	void testFindAllWithValidParamsShouldReturnStatusOk() throws Exception {
		when(this.userService.findAll(any(UserFilterDTO.class))).thenReturn(Page.empty());

		this.mockMvc.perform(get("/v1/users?page=1&perPage=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(hasSize(0)))
				.andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.totalElements").value(0));

		verify(this.userService, only()).findAll(any(UserFilterDTO.class));
	}

	@Test
	void testUpdateStatusWithInvalidIdShouldReturnStatusNotFound() throws Exception {
		var errorMessage = "SOME ERROR";

		doThrow(new NotFoundException(errorMessage)).when(this.userService).updateStatus(anyInt(), anyBoolean());

		this.mockMvc.perform(patch("/v1/users/1?active=true").with(csrf()))
				.andExpect(status().isNotFound())
				.andExpect(content().string(errorMessage));

		verify(this.userService, only()).updateStatus(anyInt(), anyBoolean());
	}

	@Test
	void testUpdateStatusWithValidIdShouldReturnStatusOk() throws Exception {
		doNothing().when(this.userService).updateStatus(anyInt(), anyBoolean());

		this.mockMvc.perform(patch("/v1/users/2?active=false").with(csrf()))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));

		verify(this.userService, only()).updateStatus(anyInt(), anyBoolean());
	}
}
