package br.com.calculatorapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.calculatorapi.model.vo.OperationVO;
import br.com.calculatorapi.repository.OperationRepository;

@Service
public class OperationService {

	private final OperationRepository operationRepository;

	public OperationService(OperationRepository operationRepository) {
		this.operationRepository = operationRepository;
	}

	public List<OperationVO> findAll() {
		return this.operationRepository.findAll().stream().map(operation -> OperationVO.builder()
				.withId(operation.getId())
				.withDescription(operation.getDescription())
				.build()
		).toList();
	}

}
