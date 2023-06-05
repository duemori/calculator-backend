package br.com.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.userapi.model.User;


public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
	boolean existsByEmailAndActiveTrue(String email);
}
