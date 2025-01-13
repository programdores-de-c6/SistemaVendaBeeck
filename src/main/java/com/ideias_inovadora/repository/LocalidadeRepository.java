package com.ideias_inovadora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Localidade;


public interface LocalidadeRepository  extends JpaRepository<Localidade, Long>{
	
	   // Buscar todos os distritos de um país
    List<Localidade> findByDistritoId(Long distritoId);
    // Buscar distrito por nome dentro de um país
    Optional<Localidade> findByNomeAndDistritoId(String nome, Long distritoId);

}
