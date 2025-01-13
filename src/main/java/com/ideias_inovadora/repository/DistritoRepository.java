package com.ideias_inovadora.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Distrito;

import jakarta.validation.constraints.AssertFalse.List;


public interface DistritoRepository extends JpaRepository<Distrito, Long> {
	
	   // Buscar todos os distritos de um país
	List findByPaisId(Long paisId);

    // Buscar distrito por nome dentro de um país
    Optional<Distrito> findByNomeAndPaisId(String nome, Long paisId);

}
