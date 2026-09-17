package com.ideias_inovadora.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByCodigobarra(String codigobarra);
	
	

}
