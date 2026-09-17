package com.ideias_inovadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.model.Tax;
import com.ideias_inovadora.repository.TaxRepository;

@Service
public class TaxService {
	
	@Autowired
	TaxRepository taxRepository;
	
	public String create(Tax tax) {
		taxRepository.save(tax);	
		return "Operação Realizada com Sucesso";
	}
	
	public String update(Tax tax) {
		taxRepository.saveAndFlush(tax);
		return ("Operação Realiada com Sucesso");
	}
	
	public Object list() {
		return taxRepository.findAll();
	}

}
