package br.com.calculatorapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.calculatorapi.model.dto.CreditDTO;
import br.com.calculatorapi.model.dto.TransactionDTO;
import br.com.calculatorapi.model.dto.TransactionFilterDTO;
import br.com.calculatorapi.model.vo.TransactionVO;
import br.com.calculatorapi.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/transactions")
@Tag(name = "Transactions API")
public class TransactionController {

	private static final String USER_ID_CLAIM = "uid";

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Creates a transaction")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Transaction created"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "422", description = "Invalid operation or insufficient credit")
	})
	public ResponseEntity<Void> create(@Valid @RequestBody @Parameter(description = "Transaction information") TransactionDTO transaction, @AuthenticationPrincipal Jwt jwt) {
		var id = this.transactionService.create(transaction, getUserId(jwt));
		var uri = UriComponentsBuilder.fromPath("/v1/transactions/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@PostMapping("/credits")
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Creates a credit transaction")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Credit transaction created"),
			@ApiResponse(responseCode = "400", description = "Bad request")
	})
	public ResponseEntity<Void> credit(@Valid @RequestBody @Parameter(description = "Credit transaction information") CreditDTO credit, @AuthenticationPrincipal Jwt jwt) {
		var id = this.transactionService.create(credit, getUserId(jwt));
		var uri = UriComponentsBuilder.fromPath("/v1/transactions/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	@Operation(summary = "List all transactions filtered and paginated")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Return transactions filtered and paginated"),
			@ApiResponse(responseCode = "400", description = "Bad request")
	})
	public ResponseEntity<Page<TransactionVO>> findAll(@Valid TransactionFilterDTO filter, @AuthenticationPrincipal Jwt jwt) {
		return ResponseEntity.ok(this.transactionService.findAll(filter, getUserId(jwt)));
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete logically the transaction specified")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Transaction not found")
	})
	public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal Jwt jwt) {
		this.transactionService.delete(id, getUserId(jwt));
		return ResponseEntity.noContent().build();
	}

	private static Integer getUserId(Jwt jwt) {
		return Integer.valueOf(jwt.getClaimAsString(USER_ID_CLAIM));
	}
}
