package com.ideias_inovadora.controller;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ideias_inovadora.dto.SaleRequestDTO;
import com.ideias_inovadora.dto.SaleResponseDTO;
import com.ideias_inovadora.model.Sale;
import com.ideias_inovadora.repository.SaleRepository;
import com.ideias_inovadora.service.SaleService;

@RestController
@RequestMapping("api/sales-system/sales/")
@CrossOrigin("*") // Permite que o React aceda à API
public class SaleController {

	@Autowired
	private SaleService saleService;
	


	@PostMapping("create")
	public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO dto) {
	    return ResponseEntity.status(HttpStatus.CREATED).body(saleService.saveSale(dto));
	}

	/**
	 * Endpoint para o Histórico do Turno (RecentSalesTable no React).
	 * Busca todas as vendas vinculadas a um ID de caixa específico.
	 */

	@GetMapping(value = "by-box/{boxId}")
    public ResponseEntity<List<SaleResponseDTO>> getSalesByBox(@PathVariable Long boxId) {
        return ResponseEntity.ok(saleService.listarVendasPorCaixa(boxId));
    }
	
	// Para quando clicar no "Olho" (Ver) ou "Impressora" (Imprimir)
    @GetMapping(value = "details/{saleId}")
    public ResponseEntity<SaleResponseDTO> getSaleDetails(@PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.obterDetalhesVenda(saleId));
    }
    /**
     * ✅ NOVO: HISTÓRICO GERENCIAL / GLOBAL (Uso do Dono ou Gerente)
     * Este endpoint lê o cabeçalho 'X-Store-ID' enviado pelo React.
     * - Se o ID for 0: O Service retorna vendas de TODAS as lojas (Modo Global).
     * - Se o ID for 1 ou 2: Retorna apenas daquela unidade.
     */
    @GetMapping("/history")
    public ResponseEntity<List<SaleResponseDTO>> getStoreHistory(
            @RequestHeader(value = "X-Store-ID", required = false) Long shopId) {
        // Chama o método listarHistorico que criamos no Service
        return ResponseEntity.ok(saleService.listarHistorico(shopId));
    }
}