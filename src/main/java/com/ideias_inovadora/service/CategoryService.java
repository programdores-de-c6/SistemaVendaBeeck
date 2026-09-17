package com.ideias_inovadora.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.ideias_inovadora.model.Category;
import com.ideias_inovadora.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Transactional
	@CacheEvict(value = "category", allEntries = true)
	public String create(Category category) {
		category.setCreatedAt(LocalDateTime.now());
		categoryRepository.save(category);
		return "Operacção realizada com Sucesso";
	}
	/**
     * Remove uma categoria do sistema.
     * ✅ @Transactional: Garante que a operação seja atómica.
     * ✅ @CacheEvict: Limpa a lista de categorias em cache para atualizar o Frontend.
     */
    @Transactional
    @CacheEvict(value = "category", allEntries = true)
    public String delete(Long id) {
        // 1. Verifica se a categoria existe antes de tentar apagar
        if (!categoryRepository.existsById(id)) {
            throw new com.ideias_inovadora.erros.OperationNotAllowedException("Categoria não encontrada para exclusão.");
        }

        try {
            // 2. Tenta a remoção física do banco de dados
            categoryRepository.deleteById(id);
            return "Categoria removida com sucesso.";
        } catch (Exception e) {
            // 3. 🛡️ PROTEÇÃO: Se houver produtos vinculados a esta categoria, o banco impede o delete.
            // Capturamos o erro e enviamos uma mensagem amigável ao utilizador.
            throw new com.ideias_inovadora.erros.OperationNotAllowedException(
                "Não é possível eliminar esta categoria porque existem produtos associados a ela."
            );
        }
    }
	@Transactional
	@CacheEvict(value = "category", allEntries = true)
	public String update(Category category) {
		category.setUpdatedAt(LocalDateTime.now());
		categoryRepository.saveAndFlush(category);
		return "Operacção realizada com Sucesso";
	}

	@Cacheable(value = "category", key = "'list'")
	public List<Category> list() {

		return categoryRepository.findAll();
	}

	

}
