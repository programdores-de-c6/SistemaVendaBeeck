package com.ideias_inovadora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideias_inovadora.model.Box;
import com.ideias_inovadora.model.StatusCaixa;

public interface BoxRepository extends JpaRepository<Box, Long> {

    // ====================================================
    // LISTAR CAIXAS DE UMA LOJA
    // ====================================================
    //
    // Agora a loja pertence directamente à entidade Box.
    //
    // Exemplo:
    // Box #10 -> shop = Loja A
    // Box #11 -> shop = Loja B
    //
    List<Box> findAllByShopIdOrderByIdDesc(Long shopId);


    // ====================================================
    // MÉTODO ANTIGO
    // ====================================================
    //
    // Mantemos caso ainda exista algum outro ponto do sistema
    // que precise procurar o último caixa de um funcionário.
    //
    Optional<Box> findFirstByEmployeeOpenedIdAndStatusCaixaOrderByIdDesc(
            long employeeId,
            StatusCaixa status
    );


    // ====================================================
    // PROCURAR CAIXA ABERTO DE UMA LOJA
    // ====================================================
    //
    // IMPORTANTE:
    // Não procuramos pelo employeeOpened.
    //
    // Procuramos:
    //     loja + estado ABERTO
    //
    // Isto resolve o caso do Admin Master.
    //
    @Query("""
        SELECT b
        FROM Box b
        WHERE b.shop.id = :shopId
          AND b.statusCaixa = :status
        ORDER BY b.id DESC
    """)
    List<Box> findActiveSessionsByShop(
            @Param("shopId") Long shopId,
            @Param("status") StatusCaixa status
    );


    // ====================================================
    // MÉTODO ANTIGO
    // ====================================================
    //
    // Este procura pelo funcionário + loja através do
    // employeeOpened.
    //
    // Pode ser mantido temporariamente se ainda existir
    // algum código que o utilize.
    //
    @Query("""
        SELECT b
        FROM Box b
        WHERE b.employeeOpened.id = :empId
          AND b.employeeOpened.shop.id = :shopId
          AND b.statusCaixa = 'ABERTO'
        ORDER BY b.id DESC
    """)
    List<Box> findActiveSessions(
            @Param("empId") Long empId,
            @Param("shopId") Long shopId
    );
}