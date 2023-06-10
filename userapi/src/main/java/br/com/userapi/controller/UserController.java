package br.com.userapi.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.userapi.model.dto.UserDTO;
import br.com.userapi.model.dto.UserFilterDTO;
import br.com.userapi.model.vo.UserVO;
import br.com.userapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "Users API")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Creates a user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "409", description = "Email already in use")
	})
	public ResponseEntity<Void> create(@Valid @RequestBody @Parameter(description = "User information") UserDTO user) {
		var id = this.userService.create(user);
		var uri = UriComponentsBuilder.fromPath("/v1/users/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	@Operation(summary = "List all users filtered and paginated")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Return users filtered and paginated"),
			@ApiResponse(responseCode = "400", description = "Bad request")
	})
	public ResponseEntity<Page<UserVO>> findAll(@Valid UserFilterDTO filter) {
		return ResponseEntity.ok(this.userService.findAll(filter));
	}

	@PatchMapping("{id}")
	@Operation(summary = "Change user status")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Status updated"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "409", description = "Another user with the same email is active")
	})
	public ResponseEntity<Void> updateStatus(@PathVariable Integer id, @RequestParam Boolean active) {
		this.userService.updateStatus(id, active);
		return ResponseEntity.noContent().build();
	}
}
