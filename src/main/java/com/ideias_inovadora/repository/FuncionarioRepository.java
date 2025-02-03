package com.ideias_inovadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
	boolean existsByLocationId(Long localidadeId);
}
