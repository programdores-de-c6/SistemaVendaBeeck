package com.ideias_inovadora.repository;

import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ideias_inovadora.model.Pais;

public interface PaisRepository extends JpaRepository<Pais, Long> {


	List<Pais> findAll();
	


	  
}