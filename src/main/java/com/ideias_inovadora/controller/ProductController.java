package com.ideias_inovadora.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ideias_inovadora.dto.ProductDTO;
import com.ideias_inovadora.dto.StockDTO;
import com.ideias_inovadora.dto.StockMovementDTO;
import com.ideias_inovadora.model.Product;
import com.ideias_inovadora.service.ProductService;
import com.ideias_inovadora.service.StockMovementService;
import com.ideias_inovadora.util.ApiResponse;

import io.swagger.v3.oas.models.media.MediaType;

@RestController
@RequestMapping("api/sales-system/product/")
public class ProductController {

	@Autowired
	ProductService productService;
	@Autowired
	ApiResponse apiResponse;
	@Autowired
	StockMovementService stockMovementService;

	
	

	
	/**
	 * Criação de Produto: O Java agora recebe o X-Store-ID para saber
	 * em qual loja deve injetar o stock inicial.
	*/
	@PostMapping(value = "create")
	public ResponseEntity<ApiResponse> create(
			@RequestPart(value = "file", required = false) MultipartFile file, 
			@RequestPart("productDTO") @Validated ProductDTO productDTO) throws Exception {
		
		productDTO.setFile(file);
		
		
		apiResponse.setMessage(productService.create(productDTO));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(
	        @RequestHeader(value = "X-Store-ID", required = false) Long shopId, // ✅ Contexto da loja atual
	        @Validated @RequestBody ProductDTO productDTO) { // ✅ Recebe DTO em vez da Entidade pura

	    // O Service agora é responsável por carregar o produto original e fazer o merge
	    apiResponse.setMessage(productService.update(productDTO, shopId));
	    apiResponse.setStatus("Sucesso");
	    
	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	@PutMapping(value = "updatestock")
	public ResponseEntity<ApiResponse> updateStock(@Validated @RequestBody ProductDTO productDTO) {
	    apiResponse.setMessage(productService.atualizarMovimentacaoStock(productDTO));
	    apiResponse.setStatus("Sucesso");
	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	/**
	 * Listagem Inteligente: 
	 * Se for um Gerente (Header X-Store-ID presente), lista apenas o stock da loja dele.
	 * Se for Admin Master (Header ausente), lista o catálogo global (Loja 1 padrão).
	 */
	@GetMapping(value = "list")
	public ResponseEntity<List<ProductDTO>> list(
			@RequestHeader(value = "X-Store-ID", required = false) Long shopId) throws Exception {
		
		if (shopId != null) {
		
			// Retorna produtos com o stock específico da Gráfica ou da Loja
			return ResponseEntity.ok(this.productService.listidloja(shopId));
		}
		// Caso contrário, lista o geral
		return ResponseEntity.ok(this.productService.list());
	}

	/**
	 * Busca por ID específico: 
	 * Também respeita o shopId para devolver o stock correto no detalhe.
	 */
	@GetMapping(value = "listid/{id}") 
	public ResponseEntity<ProductDTO> findById(
	        @PathVariable Long id,
	        @RequestHeader(value = "X-Store-ID", required = false) Long shopId) { // ✅ Captura o contexto
	    
	    // Chama o método inteligente que criámos no Service
	    ProductDTO resultado = this.productService.findByIdInteligente(id, shopId);
	            
	    return ResponseEntity.ok(resultado); 
	}

	@GetMapping(value = "moviment")
	public ResponseEntity<List<StockMovementDTO>> moviment(@RequestBody StockDTO stockDTO) throws Exception {
		return ResponseEntity.ok(this.stockMovementService.list(stockDTO.getProduct(), stockDTO.getStoreId()));
	}

	@PostMapping(value = "fetch")
	public ResponseEntity<ProductDTO> fetch(@RequestBody ProductDTO productDTO) throws Exception {
		return ResponseEntity.ok(this.productService.enviarfile(productDTO));
	}
	
	/**
	 * Endpoint de Eliminação:
	 * Corrigido para aceitar ?id=X (RequestParam) conforme o seu URL de erro.
	 */
	@DeleteMapping(value = "delete") // ✅ Removido o {id} do caminho
	public ResponseEntity<ApiResponse> delete(
	        @RequestParam("id") Long id, // ✅ Alterado de PathVariable para RequestParam
	        @RequestHeader(value = "X-Store-ID", required = false) String shopIdHeader) {
	    
	    // Tratamento seguro do contexto de loja (converte String "0" ou null para Long)
	    Long shopId = (shopIdHeader != null && !shopIdHeader.equals("0")) 
	                  ? Long.parseLong(shopIdHeader) 
	                  : null;

	    apiResponse.setMessage(productService.eliminarInteligente(id, shopId));
	    apiResponse.setStatus("Sucesso");
	    return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "upload-logo")
	public ResponseEntity<ApiResponse> uploadLogo(
	        @RequestPart("file") MultipartFile file,
	        @RequestPart("productDTO") ProductDTO dto) throws Exception {
	    
	    dto.setFile(file);
	    apiResponse.setMessage(productService.atualizarFoto(dto));
	    apiResponse.setStatus("Sucesso");
	    return ResponseEntity.ok(apiResponse);
	}
}