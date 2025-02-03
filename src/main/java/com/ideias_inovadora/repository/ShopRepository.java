package com.ideias_inovadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ideias_inovadora.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
	boolean existsByLocationId(Long localidadeId);

	boolean existsByNome(String nome);

	boolean existsByNumeroContribuite(String numeroContribuite);

	List<Shop> findAll();
	@Query("SELECT s FROM Shop s WHERE UPPER(TRIM(s.nome)) LIKE UPPER(CONCAT('%', ?1, '%')) OR s.numeroContribuite LIKE CONCAT('%', ?1, '%')")
	List<Shop> busca(String argumento);

}
