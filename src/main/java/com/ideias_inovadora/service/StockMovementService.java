package com.ideias_inovadora.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.StockMovementDTO;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.MovementType;
import com.ideias_inovadora.model.Product;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.StockMovement;
import com.ideias_inovadora.repository.ProductRepository;
import com.ideias_inovadora.repository.ShopRepository;
import com.ideias_inovadora.repository.StockMovementRepository;
import com.ideias_inovadora.repository.StockRepository;

import jakarta.transaction.Transactional;


@Service
public class StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;
    
    @Autowired
     StockRepository stockRepository;



    @Transactional
    public void registerMovement(Product product, Shop shop, int quantidade, 
                                 String motivo, MovementType type, Employee employee) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setShop(shop);
        movement.setQuantidade(quantidade);
        movement.setMotivo(motivo);
        movement.setMovementType(type);
        movement.setEmployee(employee);
        movement.setData(LocalDateTime.now());

        stockMovementRepository.save(movement);
    }
   
    public List<StockMovementDTO> list(Long productId, long shopId) {
        return stockMovementRepository.findByProductIdAndShopIdOrderByDataDesc(productId, shopId)
                .stream()
                .map(this::mapToStockMovementDTO)
                .collect(Collectors.toList());
    }

    private StockMovementDTO mapToStockMovementDTO(StockMovement movement) {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setId(movement.getId());
        dto.setMotivo(movement.getMotivo());
        dto.setQuantidade(movement.getQuantidade());
        dto.setMovementType(movement.getMovementType());
        dto.setData(movement.getData());
        dto.setProduct(movement.getProduct().getNome());
        dto.setShop(movement.getShop().getShopType().name());
        dto.setEmployeeName(movement.getEmployee() != null ? movement.getEmployee().getNome() : null);
        return dto;
    }

	public List<Shop> listShopsByProduct(Long productId) {
		
	
		    return stockRepository.findShopsWithStockByProductId(productId);
	}

}
