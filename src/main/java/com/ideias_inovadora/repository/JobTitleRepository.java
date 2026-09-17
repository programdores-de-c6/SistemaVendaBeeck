package com.ideias_inovadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.JobTitle;

public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {

	
    // Retorna todos os registros de BaseSalary ordenados pelo campo 'valor' de forma ascendente
    List<JobTitle> findAllByOrderByValorAsc();

}
