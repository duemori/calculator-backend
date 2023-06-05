package br.com.calculatorapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.calculatorapi.model.vo.OperationVO;
import br.com.calculatorapi.service.OperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/operations")
@Api(tags = "Operations API")
public class OperationController {

	private final OperationService operationService;

	public OperationController(OperationService operationService) {
		this.operationService = operationService;
	}

	@GetMapping
	@ApiOperation(value = "List all available operations")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return available operations") })
	public ResponseEntity<List<OperationVO>> getAll() {
		return ResponseEntity.ok(this.operationService.findAll());
	}

}
