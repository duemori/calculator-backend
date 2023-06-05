package br.com.calculatorapi.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/transactions")
@Api(tags = "Transactions API")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation(value = "Creates a transaction")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Transaction created"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 422, message = "Invalid operation or insufficient credit")
	})
	public ResponseEntity<Void> create(@Valid @RequestBody @ApiParam(value = "Transaction information") TransactionDTO transaction) {
		var id = this.transactionService.create(transaction);
		var uri = UriComponentsBuilder.fromPath("/v1/transactions/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@PostMapping("/credits")
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation(value = "Creates a credit transaction")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Credit transaction created"),
			@ApiResponse(code = 400, message = "Bad request")
	})
	public ResponseEntity<Void> credit(@Valid @RequestBody @ApiParam(value = "Credit transaction information") CreditDTO credit) {
		var id = this.transactionService.create(credit);
		var uri = UriComponentsBuilder.fromPath("/v1/transactions/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	@ApiOperation(value = "List all transactions filtered and paginated")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transactions filtered and paginated"),
			@ApiResponse(code = 400, message = "Bad request")
	})
	public ResponseEntity<Page<TransactionVO>> findAll(@Valid TransactionFilterDTO filter) {
		return ResponseEntity.ok(this.transactionService.findAll(filter));
	}

	@DeleteMapping("{id}")
	@ApiOperation(value = "Delete logically the transaction specified")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deleted successfully"),
			@ApiResponse(code = 404, message = "Transaction not found")
	})
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		this.transactionService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
