package com.ideias_inovadora.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	boolean existsByLocationId(Long localidadeId);
	
	 Optional<Customer> findByNumeroContribuinte(String numeroContribuinte);
}
