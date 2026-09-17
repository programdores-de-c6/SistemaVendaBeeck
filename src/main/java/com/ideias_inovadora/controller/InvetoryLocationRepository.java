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
import com.ideias_inovadora.model.InventoryLocation;
import com.ideias_inovadora.service.InventoryLocationService;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/invetoryLocation/")
public class InvetoryLocationRepository {
	@Autowired
	InventoryLocationService inventoryLocationService;
	@Autowired
	ApiResponse apiResponse;

	@PostMapping(value = "create")
	public ResponseEntity<ApiResponse> create(@Validated @RequestBody InventoryLocation inventoryLocation) {
		apiResponse.setMessage(inventoryLocationService.create(inventoryLocation));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody InventoryLocation inventoryLocation) {
		apiResponse.setMessage(inventoryLocationService.update(inventoryLocation));
		apiResponse.setStatus("Sucesso");

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}

	@GetMapping(value = "list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(this.inventoryLocationService.list());

	}

}