package com.ideias_inovadora.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.JobTitle;
import com.ideias_inovadora.repository.JobTitleRepository;

@Service

public class JobTitleService {
	@Autowired
	JobTitleRepository jobTitleRepository;
	@CacheEvict(value = "salary", allEntries = true)
	public String create(JobTitle jobTitle) {

	    if (jobTitle.getAccessLevel() == null) {
	        throw new IllegalArgumentException(
	                "Nível de acesso é obrigatório."
	        );
	    }

	    jobTitle.setDatacricao(LocalDateTime.now());

	    jobTitleRepository.save(jobTitle);

	    return "Cargo criado com sucesso.";
	}

	@CacheEvict(value = "salary", allEntries = true)
	public String update(JobTitle jobTitle) {
		if (!existsByISalaryBase(jobTitle.getId())) {
			throw new OperationNotAllowedException("Operação não realizada.... ");

		}
		jobTitle.setDataActualizacao(LocalDateTime.now());
		jobTitleRepository.saveAndFlush(jobTitle);

		return "Opreação Concluida com suceso";
	}

	@Cacheable(value = "salary", key = "'list'")
	public List<JobTitle> list() {
		return jobTitleRepository.findAll();
	}

	// Metudo para verificar id da loja
	private boolean existsByISalaryBase(Long id) {
		return jobTitleRepository.existsById(id);
	}
}
