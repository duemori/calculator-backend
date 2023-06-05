package br.com.userapi.controller;

import javax.validation.Valid;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/users")
@Api(tags = "Users API")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation(value = "Creates a user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "User created"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 409, message = "Email already in use")
	})
	public ResponseEntity<Void> create(@Valid @RequestBody @ApiParam(value = "User information") UserDTO user) {
		var id = this.userService.create(user);
		var uri = UriComponentsBuilder.fromPath("/v1/users/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	@ApiOperation(value = "List all users filtered and paginated")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return users filtered and paginated"),
			@ApiResponse(code = 400, message = "Bad request")
	})
	public ResponseEntity<Page<UserVO>> findAll(@Valid UserFilterDTO filter) {
		return ResponseEntity.ok(this.userService.findAll(filter));
	}

	@PatchMapping("{id}")
	@ApiOperation(value = "Change user status")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Status updated"),
			@ApiResponse(code = 404, message = "User not found"),
			@ApiResponse(code = 409, message = "Another user with the same email is active"),
	})
	public ResponseEntity<Void> updateStatus(@PathVariable Integer id, @RequestParam Boolean active) {
		this.userService.updateStatus(id, active);
		return ResponseEntity.noContent().build();
	}
}
