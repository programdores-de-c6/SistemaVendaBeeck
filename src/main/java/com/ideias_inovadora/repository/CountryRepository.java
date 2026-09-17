package com.ideias_inovadora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {


	List<Country> findAll();
	
	
	


	  
}