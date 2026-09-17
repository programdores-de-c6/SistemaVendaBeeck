package com.ideias_inovadora.repository;



import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ideias_inovadora.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Busca todas as transações de um caixa específico
    List<Transaction> findByBoxId(Long boxId);
    
    // Busca as transações de uma loja específica
    List<Transaction> findByShopId(Long shopId);
    
    // ✅ Soma o total de todas as transações de venda vinculadas a um caixa específico
    @Query("SELECT SUM(t.totalGeral) FROM Transaction t WHERE t.box.id = :boxId AND t.transactionType = 'VENDA'")
    BigDecimal sumVendasByBoxId(@Param("boxId") Long boxId);
}