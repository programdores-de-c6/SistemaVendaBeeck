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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ideias_inovadora.dto.ShopDTO;
import com.ideias_inovadora.dto.ShopDTOs;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.service.ShopSevice;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/shop/")
public class ShopController {

	@Autowired
	ShopSevice shopSevice;
	@Autowired
	ApiResponse apiResponse;

	@PostMapping(value = "create")
	public ResponseEntity<ApiResponse> create(@Validated @RequestBody Shop shop) {
		apiResponse.setMessage(shopSevice.create(shop));
		apiResponse.setStatus("Sucesso");

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody Shop shop) {
		apiResponse.setMessage(shopSevice.update(shop));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}

	@GetMapping(value = "list")
	public ResponseEntity<List<ShopDTO>> list() throws Exception {
		return ResponseEntity.ok(this.shopSevice.list());

	}

	@GetMapping(value = "lists")
	public ResponseEntity<List<ShopDTOs>> listCombox() throws Exception {
		return ResponseEntity.ok(this.shopSevice.listCompobox());

	}

	@GetMapping(value = "search")
	public ResponseEntity<List<ShopDTO>> search(@RequestParam(name = "nome") String nome) throws Exception {
		return ResponseEntity.ok(this.shopSevice.search(nome));

	}
}
