package com.ideias_inovadora.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.dto.BoxRequestDTO;
import com.ideias_inovadora.dto.BoxResponseDTO;
import com.ideias_inovadora.model.Box;
import com.ideias_inovadora.service.BoxService;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/box/")
public class BoxController {

	
	@Autowired
	BoxService boxService;
	
	@Autowired
	ApiResponse apiResponse;

	@PostMapping(value = "create")
	public ResponseEntity<BoxResponseDTO> create(
	        @RequestBody Box box,
	        @RequestHeader(
	                value = "X-Store-ID",
	                required = false
	        ) Long shopId) {

	    BoxResponseDTO response =
	            boxService.create(
	                    box,
	                    shopId
	            );

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(response);
	}

	
	@GetMapping(value = "active-session")
	public ResponseEntity<BoxResponseDTO> getActiveSession(
	        @RequestParam("utilizador_id") Long employeeId,
	        @RequestHeader(value = "X-Store-ID", required = false) String shopIdHeader) {

	    // Sem loja seleccionada, não existe caixa operacional.
	    if (shopIdHeader == null ||
	        shopIdHeader.isEmpty() ||
	        shopIdHeader.equals("0")) {

	        return ResponseEntity.noContent().build();
	    }

	    try {

	        // Converte o ID da loja recebido no header.
	        Long shopId = Long.parseLong(shopIdHeader);

	        // Procura o caixa aberto da loja actual.
	        BoxResponseDTO activeBoxDTO =
	                boxService.findActiveSession(employeeId, shopId);

	        // Se encontrou, devolve o caixa.
	        if (activeBoxDTO != null) {
	            return ResponseEntity.ok(activeBoxDTO);
	        }

	        // Se não encontrou, informa que não há caixa aberto.
	        return ResponseEntity.noContent().build();

	    } catch (NumberFormatException e) {

	        // Header com ID de loja inválido.
	        return ResponseEntity.badRequest().build();
	    }
	}
		
		@PostMapping("fechar")
		public ResponseEntity<BoxResponseDTO> fechar(@RequestBody BoxRequestDTO dto) {
		    return ResponseEntity.ok(boxService.fecharCaixa(dto));
		}

		

		// ✅ FILTRADO: Listagem de turnos por loja
		@GetMapping("list-all")
		public ResponseEntity<List<BoxResponseDTO>> listAll(
		        @RequestHeader(value = "X-Store-ID", required = false) Long shopId) {
		    
		    // Se o Admin estiver numa loja, filtra. Se estiver no Global (null/0), mostra tudo.
		    if (shopId != null && shopId != 0) {
		        return ResponseEntity.ok(boxService.listarPorLoja(shopId));
		    }
		    return ResponseEntity.ok(boxService.listarTodos());
		}
		
		
}