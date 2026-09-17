package com.ideias_inovadora.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.Stock;

import jakarta.transaction.Transactional;

public interface StockRepository extends JpaRepository<Stock, Long>{

	Optional<Stock> findByShopIdAndProductId(long shop, long product);
	
    List<Stock> findByShopId(Long shopId);

	//Optional<Stock> findByShopIdAndProductId(long id, long i);

	 //Optional<Stock> findByShopIdAndProductId(Long productId , Long shopId );
    
 // Verifica se o produto está associado a alguma loja
    boolean existsByProductId(Long productId);
    
    @Query("SELECT s.shop FROM Stock s WHERE s.product.id = :productId AND s.quantidade > 0")
    List<Shop> findShopsWithStockByProductId(@Param("productId") Long productId);

        @Modifying
        @Transactional
        @Query("UPDATE Stock s SET s.precoUnitario = :preco WHERE s.product.id = :productId AND s.shop.id = :shopId")
        void updatePrecoLocal(@Param("preco") BigDecimal preco, @Param("productId") Long productId, @Param("shopId") Long shopId);
    
    
}
