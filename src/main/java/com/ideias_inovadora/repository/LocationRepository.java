package com.ideias_inovadora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Location;


public interface LocationRepository  extends JpaRepository<Location, Long>{
	
	   // Buscar todas as localidades
    List<Location> findAll();
    // Busca por nome (case-insensitive e parcial)
    List<Location> findByNomeContainingIgnoreCase(String nome);
}
