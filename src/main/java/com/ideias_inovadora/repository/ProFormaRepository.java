package com.ideias_inovadora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideias_inovadora.model.ProForma;

public interface ProFormaRepository extends JpaRepository<ProForma, Long> {
    @EntityGraph(attributePaths = {"itens", "itens.product", "customer", "shop"})
    Optional<ProForma> findById(Long id);

    @EntityGraph(attributePaths = {"itens", "itens.product", "customer", "shop"})
    List<ProForma> findByShopIdOrderByDataproformaDesc(Long shopId);

    List<ProForma> findByShopIdAndEstadoOrderByDataproformaDesc(Long shopId, String estado);
    
    List<ProForma> findAllByOrderByDataproformaDesc();
    
    

    List<ProForma> findAllByShopIdAndNumeroproformaStartingWith(
            Long shopId,
            String prefix
    );
    @Query(value = """
    	    SELECT COALESCE(
    	        MAX(
    	            CAST(
    	                SUBSTRING_INDEX(numeroproforma, '/', 1)
    	                AS UNSIGNED
    	            )
    	        ),
    	        0
    	    )
    	    FROM pro_forma
    	    WHERE shop_fk = :shopId
    	      AND numeroproforma LIKE CONCAT('%/', :ano)
    	    """, nativeQuery = true)
    	Integer findUltimoNumeroProForma(
    	        @Param("shopId") Long shopId,
    	        @Param("ano") int ano
    	);
}
