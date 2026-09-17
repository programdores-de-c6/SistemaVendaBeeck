package com.ideias_inovadora.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.SupplierDTOs;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.Supplier;
import com.ideias_inovadora.repository.SupplierRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierService {
	@Autowired
	SupplierRepository supplierRepository;
	@Transactional
	@CacheEvict(value = "supplier", allEntries = true)
	public String create(SupplierDTOs supplierDTOs) {

		Supplier supplier = crateobjecto(supplierDTOs);
		supplier.setDatacriacao(LocalDateTime.now());
		supplierRepository.save(supplier);

		return "Operação Realizada com Sucesso";
	}
	/**
     * Remove um fornecedor do sistema.
     * ✅ @CacheEvict: Limpa a lista cacheada para que o Frontend veja a mudança.
     */
    @Transactional
    @CacheEvict(value = "supplier", allEntries = true)
    public String delete(Long id) {
        // 1. Verifica se o fornecedor existe na base de dados
        if (!supplierRepository.existsById(id)) {
            throw new OperationNotAllowedException("Fornecedor não encontrado para exclusão.");
        }

        try {
            // 2. Tenta a exclusão física
            supplierRepository.deleteById(id);
            return "Fornecedor removido com sucesso.";
        } catch (Exception e) {
            // 3. 🛡️ TRATAMENTO DE INTEGRIDADE: Se houver produtos ligados a este fornecedor, 
            // o banco vai lançar uma DataIntegrityViolationException.
            throw new OperationNotAllowedException("Não é possível eliminar este fornecedor porque existem produtos associados a ele. Recomenda-se apenas desativar.");
        }
    }
	@Transactional
	@CacheEvict(value = "supplier", allEntries = true)
	public String update(SupplierDTOs supplierDTOs) {
		Supplier supplier = crateobjecto(supplierDTOs);
		supplier.setId(supplierDTOs.getId());
		supplier.setDataAtualizacao(LocalDateTime.now());
		supplierRepository.saveAndFlush(supplier);
		return "Operação Realizada com Sucesso";
	}

	@Cacheable(value = "supplier", key = "'list'")
	public List<SupplierDTOs> list() {
		return supplierRepository.findAll().stream().map(this::mapToSupplierDTOs).collect(Collectors.toList());
	}

	private SupplierDTOs mapToSupplierDTOs(Supplier supplier1) {
		SupplierDTOs supplierDTOs = new SupplierDTOs();
		supplierDTOs.setId(supplier1.getId());
		supplierDTOs.setNome(supplier1.getNome());
		supplierDTOs.setContactoPrincipal(supplier1.getContactoPrincipal());
		supplierDTOs.setContactoSecudario(supplier1.getContactoSecudario());
		supplierDTOs.setEmail(supplier1.getEmail());
		supplierDTOs.setNumeroContribuite(supplier1.getNumeroContribuinte());
		supplierDTOs.setNomepais(supplier1.getCountry().getNome());
		supplierDTOs.setIdpais(supplier1.getCountry().getId());

		return supplierDTOs;
	}

	private Supplier crateobjecto(SupplierDTOs supplierDTOs) {

		Supplier supplier = new Supplier();
		supplier.setNome(supplierDTOs.getNome());
		supplier.setContactoPrincipal(supplierDTOs.getContactoPrincipal());
		supplier.setContactoSecudario(supplierDTOs.getContactoSecudario());
		supplier.setNumeroContribuinte(supplierDTOs.getNumeroContribuite());
		supplier.setEmail(supplierDTOs.getEmail());
		supplier.setCountry(supplierDTOs.getCountry());
		return supplier;

	}

}
