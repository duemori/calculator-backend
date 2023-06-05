package br.com.calculatorapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.calculatorapi.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
	List<Transaction> findAllByUserIdAndActiveTrue(Integer userId);
}
