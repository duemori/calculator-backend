package br.com.calculatorapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.calculatorapi.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
