package com.ideias_inovadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.dto.SupplierDTOs;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.service.SupplierService;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/supplier") 
public class SupplierController {
	
@Autowired
SupplierService supplierService;
@Autowired
ApiResponse apiResponse;

  @PostMapping(value = "create")
  public ResponseEntity<ApiResponse> create(@Validated @RequestBody SupplierDTOs supplierDTOs){
	  apiResponse.setMessage(supplierService.create(supplierDTOs));
	  apiResponse.setStatus("Sucesso");
	  return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
  }
  
  /**
   * Endpoint para eliminar um fornecedor pelo ID.
   * Rota: DELETE api/sales-system/supplier/delete/1
   */
  @DeleteMapping(value = "delete/{id}")
  public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
      // 1. O Service executa a remoção e verifica a integridade referencial
      apiResponse.setMessage(supplierService.delete(id));
      
      // 2. Define o status da resposta global
      apiResponse.setStatus("Sucesso");
      
      // 3. Retorna HTTP 200 OK com os detalhes da operação
      return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }
  @PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody SupplierDTOs supplierDTOs) {
		apiResponse.setMessage(supplierService.update(supplierDTOs));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}
  
	@GetMapping(value = "list")
	public ResponseEntity<List<SupplierDTOs>> list() throws Exception {
		return ResponseEntity.ok(this.supplierService.list());

	}
	

}
