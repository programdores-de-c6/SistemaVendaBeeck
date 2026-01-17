package com.ideias_inovadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Distrito;

public interface DistritoRepository extends JpaRepository<Distrito, Long> {

	// listar todos os distritos

	List<Distrito> findAll();
}
