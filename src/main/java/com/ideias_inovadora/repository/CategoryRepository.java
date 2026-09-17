package com.ideias_inovadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
