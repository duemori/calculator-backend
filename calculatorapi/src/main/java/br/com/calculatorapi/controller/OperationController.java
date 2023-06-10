package br.com.calculatorapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.calculatorapi.model.vo.OperationVO;
import br.com.calculatorapi.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/operations")
@Tag(name = "Operations API")
public class OperationController {

	private final OperationService operationService;

	public OperationController(OperationService operationService) {
		this.operationService = operationService;
	}

	@GetMapping
	@Operation(summary = "List all available operations")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Return available operations") })
	public ResponseEntity<List<OperationVO>> getAll() {
		return ResponseEntity.ok(this.operationService.findAll());
	}

}
