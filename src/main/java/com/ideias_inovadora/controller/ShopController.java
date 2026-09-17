package com.ideias_inovadora.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ideias_inovadora.dto.ShopDTO;
import com.ideias_inovadora.service.ShopSevice;
import com.ideias_inovadora.util.ApiResponse;


/**
 * CONTROLADOR DE UNIDADES (SHOP)
 * Responsável por gerir a Gráfica, Livraria e Armazéns.
 */
@RestController
@RequestMapping("api/sales-system/shop") // Removida barra final redundante
public class ShopController {

    @Autowired
    private ShopSevice shopSevice;
    
    @Autowired
    private ApiResponse apiResponse;

    /**
     * Regista uma nova unidade (Apenas Admin Master).
     */
    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> create(
            @RequestPart(value = "file", required = false) MultipartFile file, 
            @RequestPart("shopDto") @Validated ShopDTO shopDto) throws Exception {
        
        shopDto.setFile(file);
        apiResponse.setMessage(shopSevice.create(shopDto));
        apiResponse.setStatus("Sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    
    
    @PutMapping(value = "/update")
    public ResponseEntity<ApiResponse> update(@Validated @RequestBody com.ideias_inovadora.model.Shop shop) {
        apiResponse.setMessage(shopSevice.update(shop));
        apiResponse.setStatus("Sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * Lista todas as lojas para o Administrador Geral.
     */
    @GetMapping(value = "/list")
    public ResponseEntity<List<ShopDTO>> list() throws Exception {
        return ResponseEntity.ok(this.shopSevice.list());
    }

    /**
     * Detalhes de uma loja específica (útil para cabeçalhos de fatura).
     */
    @GetMapping(value = "/listid/{id}") 
    public ResponseEntity<ShopDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(this.shopSevice.listid(id)); 
    }

    /**
     * Endpoint técnico para buscar o logótipo da loja no Login.
     */
    @PostMapping(value = "/fetch")
    public ResponseEntity<ShopDTO> fetch(@RequestBody ShopDTO shopDTO) throws Exception {
        return ResponseEntity.ok(this.shopSevice.enviarfile(shopDTO));
    }
  
 
    
   @PostMapping(value = "/upload-logo")
   public ResponseEntity<ApiResponse> uploadLogo(
           @RequestPart("id") String id, // Recebemos como String para evitar erro de parse
           @RequestPart("file") MultipartFile file) throws Exception {

       // Converte o ID para Long e monta o DTO para o serviço
       ShopDTO dto = new ShopDTO();
       dto.setId(Long.parseLong(id));
       dto.setFile(file);

       // Chama a lógica de negócio
       apiResponse.setMessage(shopSevice.upload_logo(dto));
       apiResponse.setStatus("Sucesso");

       return ResponseEntity.ok(apiResponse);
   }
}