package com.ideias_inovadora.repository;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ideias_inovadora.model.Pais;

public interface PaisRepository extends JpaRepository<Pais, Log> {

	// Buscar por nome (exato)
	Optional<Pais> findByNome(String nome);

	// Buscar por sigla (exata)
	Optional<Pais> findBySigla(String sigla);

}
