package com.ideias_inovadora.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.ideias_inovadora.model.Category;
import com.ideias_inovadora.service.CategoryService;
import com.ideias_inovadora.util.ApiResponse;

@RestController
@RequestMapping("api/sales-system/category/")
public class CategoryController {

	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ApiResponse apiResponse;

	
	@PostMapping(value = "create")
	public ResponseEntity<ApiResponse> create(@Validated @RequestBody Category category) {
		apiResponse.setMessage(categoryService.create(category));
		apiResponse.setStatus("Sucesso");
		System.out.print(apiResponse);

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody Category category) {
		apiResponse.setMessage(categoryService.update(category));
		apiResponse.setStatus("Sucesso");
	
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}
	/**
     * Endpoint para eliminar uma categoria via ID.
     * Rota: DELETE api/sales-system/category/delete/{id}
     */
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        // 1. Chama o serviço de exclusão
        String mensagem = categoryService.delete(id);
        
        // 2. Prepara a resposta de sucesso para o componente Toast do React
        apiResponse.setMessage(mensagem);
        apiResponse.setStatus("Sucesso");
        
        // 3. Retorna HTTP 200 OK com os detalhes
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
	
	@GetMapping(value = "list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(this.categoryService.list());

	}

}
