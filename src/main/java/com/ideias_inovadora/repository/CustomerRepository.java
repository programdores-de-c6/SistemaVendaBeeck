package com.ideias_inovadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	boolean existsByLocationId(Long localidadeId);
}
