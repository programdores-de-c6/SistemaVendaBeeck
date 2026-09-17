package com.ideias_inovadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.District;

public interface DistrictRepository extends JpaRepository<District, Long> {

	// listar todos os distritos

	List<District> findAll();
}
