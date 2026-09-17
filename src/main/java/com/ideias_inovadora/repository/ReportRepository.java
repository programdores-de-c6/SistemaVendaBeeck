package com.ideias_inovadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideias_inovadora.model.Box;
import com.ideias_inovadora.model.Sale;

public interface ReportRepository
        extends JpaRepository<Sale, Long> {

    // ====================================================
    // VENDAS
    // ====================================================

    @Query("""
        SELECT s
        FROM Sale s
        JOIN s.transaction t
        WHERE t.shop.id = :shopId
        ORDER BY s.datavenda DESC
    """)
    List<Sale> findSalesByShop(
            @Param("shopId") Long shopId
    );


    @Query("""
        SELECT s
        FROM Sale s
        JOIN s.transaction t
        WHERE t.shop.id = :shopId
          AND t.box.id = :boxId
        ORDER BY s.datavenda DESC
    """)
    List<Sale> findSalesByShopAndBox(
            @Param("shopId") Long shopId,
            @Param("boxId") Long boxId
    );


    @Query("""
        SELECT s
        FROM Sale s
        JOIN s.transaction t
        JOIN t.box b
        JOIN b.employeeOpened e
        WHERE t.shop.id = :shopId
          AND e.id = :employeeId
        ORDER BY s.datavenda DESC
    """)
    List<Sale> findSalesByShopAndEmployee(
            @Param("shopId") Long shopId,
            @Param("employeeId") Long employeeId
    );


    @Query("""
        SELECT s
        FROM Sale s
        JOIN s.transaction t
        WHERE t.box.id = :boxId
        ORDER BY s.datavenda DESC
    """)
    List<Sale> findSalesByBox(
            @Param("boxId") Long boxId
    );


    @Query("""
        SELECT s
        FROM Sale s
        ORDER BY s.datavenda DESC
    """)
    List<Sale> findAllSales();


    // ====================================================
    // CAIXAS
    // ====================================================

    @Query("""
        SELECT b
        FROM Box b
        ORDER BY b.dataAbertura DESC
    """)
    List<Box> findAllBoxes();


    @Query("""
        SELECT b
        FROM Box b
        WHERE b.shop.id = :shopId
        ORDER BY b.dataAbertura DESC
    """)
    List<Box> findBoxesByShop(
            @Param("shopId") Long shopId
    );


    // ====================================================
    // VENDAS DE UM CAIXA
    // ====================================================

    @Query("""
        SELECT s
        FROM Sale s
        JOIN s.transaction t
        WHERE t.box.id = :boxId
        ORDER BY s.datavenda ASC
    """)
    List<Sale> findSalesByBoxAsc(
            @Param("boxId") Long boxId
    );
}