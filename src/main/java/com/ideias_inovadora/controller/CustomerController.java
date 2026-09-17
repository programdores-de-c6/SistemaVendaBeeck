package com.ideias_inovadora.controller;

import java.util.List;

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

import com.ideias_inovadora.dto.CustomerDTOs;
import com.ideias_inovadora.model.Customer;
import com.ideias_inovadora.service.CustomerService;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/customer") 
public class CustomerController{
	
@Autowired
CustomerService customerService;
@Autowired
ApiResponse apiResponse;

  @PostMapping(value = "create")
  public ResponseEntity<ApiResponse> create(@Validated @RequestBody Customer customer){
	  apiResponse.setMessage(customerService.create(customer));
	  apiResponse.setStatus("Sucesso");
	  return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
  }
  
 
  @PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody Customer customer) {
		apiResponse.setMessage(customerService.update(customer));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}
  
	@GetMapping(value = "list")
	public ResponseEntity<List<CustomerDTOs>> list() throws Exception {
		return ResponseEntity.ok(this.customerService.list());

	}
	

}
