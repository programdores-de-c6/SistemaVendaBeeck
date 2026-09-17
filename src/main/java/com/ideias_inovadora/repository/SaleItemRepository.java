package com.ideias_inovadora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ideias_inovadora.model.SaleItem;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    
	 // Busca os itens vinculados a uma fatura específica
    // (Considerando que no Model você mudou para: private Sale sale)
    List<SaleItem> findBySale_Id(Long saleId);
    
    
    /**
     * ✅ VERIFICAÇÃO DE INTEGRIDADE:
     * Verifica se existe algum item de venda para este produto nesta loja específica.
     */
    @Query("SELECT COUNT(si) > 0 FROM SaleItem si " +
           "WHERE si.product.id = :productId " +
           "AND si.sale.transaction.shop.id = :shopId")
    boolean existsByProductIdAndShopId(@Param("productId") Long productId, @Param("shopId") Long shopId);
}