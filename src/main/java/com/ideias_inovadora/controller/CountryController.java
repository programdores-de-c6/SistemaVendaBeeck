package com.ideias_inovadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.repository.CountryRepository;

@RestController
@RequestMapping("api/sales-system/country") // Removida a barra no final
public class CountryController {

	@Autowired
	private CountryRepository countryRepository;

	@GetMapping("/list") // Melhor prática: Usar "/" no @GetMapping
	public ResponseEntity<?> listar() {
		return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.OK);
	}

}
