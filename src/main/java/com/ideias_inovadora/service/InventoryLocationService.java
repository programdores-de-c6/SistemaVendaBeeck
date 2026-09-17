package com.ideias_inovadora.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.model.InventoryLocation;
import com.ideias_inovadora.repository.InventoryLocationRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryLocationService {
	
	@Autowired
	InventoryLocationRepository inventoryLocationRepository;
	@Transactional
	@CacheEvict(value = "investory", allEntries = true)
	public String create(InventoryLocation inventoryLocation) {
		inventoryLocation.setDatacriacao(LocalDateTime.now());
		inventoryLocationRepository.save(inventoryLocation);
		return "Operação realizada com sucesso";
	}
	@Transactional
	@CacheEvict(value = "investory", allEntries = true)
	public String update (InventoryLocation inventoryLocation) {
		inventoryLocation.setDataAtualizacao(LocalDateTime.now());
		inventoryLocationRepository.saveAndFlush(inventoryLocation);
		return "Operação realizada com sucesso";
	}
	@Cacheable(value = "location", key = "'list'")
	public Object list() {
		return inventoryLocationRepository.findAll();
	}

}
