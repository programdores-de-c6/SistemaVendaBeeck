package com.ideias_inovadora.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ideias_inovadora.model.Series;


public interface SerieRepository extends JpaRepository<Series, Long> {
    // Busca a série baseada na loja e no ano (Ex: Loja 1, Ano 2026)
    Optional<Series> findByShopIdAndAno(long shopId, int ano);
}