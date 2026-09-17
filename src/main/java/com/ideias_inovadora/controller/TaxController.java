package com.ideias_inovadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ideias_inovadora.model.Tax;
import com.ideias_inovadora.service.TaxService;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/tax/") 
public class TaxController {
	@Autowired
	ApiResponse apiResponse;
	@Autowired
	TaxService taxService;


	@PostMapping(value = "create")
	public ResponseEntity<ApiResponse> create(@Validated @RequestBody Tax tax) {
		apiResponse.setMessage(taxService.create(tax));
		apiResponse.setStatus("Sucesso");
		System.out.print(apiResponse);

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}
	
	@PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody Tax tax) {
		apiResponse.setMessage(taxService.update(tax));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}
	
	@GetMapping(value = "list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(this.taxService.list());

	}
}
