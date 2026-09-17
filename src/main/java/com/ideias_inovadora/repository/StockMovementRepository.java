package com.ideias_inovadora.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideias_inovadora.model.StockMovement;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    // ====================================================
    // MOVIMENTOS DE UM PRODUTO NUMA LOJA
    // ====================================================

    List<StockMovement>
    findByProductIdAndShopIdOrderByDataDesc(
            Long productId,
            Long shopId
    );


    // ====================================================
    // MOVIMENTOS DE UMA LOJA NUM PERÍODO
    // ====================================================

    @Query("""
        SELECT sm
        FROM StockMovement sm
        WHERE sm.shop.id = :shopId
          AND sm.data >= :from
          AND sm.data < :to
        ORDER BY sm.data DESC
    """)
    List<StockMovement> findByShopAndDateRange(
            @Param("shopId") Long shopId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );


    // ====================================================
    // TODOS OS MOVIMENTOS DE UMA LOJA
    // ====================================================

    @Query("""
        SELECT sm
        FROM StockMovement sm
        WHERE sm.shop.id = :shopId
        ORDER BY sm.data DESC
    """)
    List<StockMovement> findAllByShop(
            @Param("shopId") Long shopId
    );


    // ====================================================
    // TODOS OS MOVIMENTOS NUM PERÍODO
    // ====================================================

    @Query("""
        SELECT sm
        FROM StockMovement sm
        WHERE sm.data >= :from
          AND sm.data < :to
        ORDER BY sm.data DESC
    """)
    List<StockMovement> findByDateRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );


    // ====================================================
    // TODOS OS MOVIMENTOS
    // ====================================================

    @Query("""
        SELECT sm
        FROM StockMovement sm
        ORDER BY sm.data DESC
    """)
    List<StockMovement> findAllGlobal();
}