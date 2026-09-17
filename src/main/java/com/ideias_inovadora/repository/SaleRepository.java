package com.ideias_inovadora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ideias_inovadora.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    /**
     * Esta consulta é o que faz a sua tela de Histórico funcionar.
     * Ela busca a Sale (Venda) através do relacionamento com a Transaction
     * filtrando pelo ID do Caixa (Box).
     */
    @Query("SELECT s FROM Sale s JOIN s.transaction t WHERE t.box.id = :boxId ORDER BY t.datatrasacao DESC")
    List<Sale> findByBoxId(@Param("boxId") Long boxId);


    /**
     *  Busca vendas por Loja (Visão de Unidade).
     */
    @Query("SELECT s FROM Sale s JOIN s.transaction t WHERE t.shop.id = :shopId ORDER BY s.datavenda DESC")
    List<Sale> findAllByShopId(@Param("shopId") Long shopId);

    /**
     *  Busca todas as vendas (Visão Global do Dono).
     */
    @Query("SELECT s FROM Sale s ORDER BY s.datavenda DESC")
    List<Sale> findAllGlobal();

    
    // Busca venda pelo número da fatura
    Sale findByNumeroFactura(String numeroFactura);
}