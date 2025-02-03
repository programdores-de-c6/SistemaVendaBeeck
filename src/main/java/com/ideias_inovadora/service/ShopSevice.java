package com.ideias_inovadora.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.ShopDTO;
import com.ideias_inovadora.dto.ShopDTOs;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.repository.LocationRepository;
import com.ideias_inovadora.repository.ShopRepository;

import jakarta.transaction.Transactional;

@Service
public class ShopSevice {

	@Autowired
	ShopRepository shopRepository;
	@Autowired
	LocationRepository locationRepository;
	
	@Transactional
	@CacheEvict(value = "shop", allEntries = true)
	public String create(Shop shop) {
		// Verifica se já existe uma loja com o mesmo nome ou número de contribuinte
		if (existsShop(shop.getNome(), shop.getNumeroContribuite())) {

			// Se existir uma loja com o nome ou número de contribuinte, lança uma exceção
			throw new OperationNotAllowedException("Erro ao salvar loja: Nome ou número de contribuinte já existe.");
		}
		// Se não existirem, salva a loja
		shop.setDatacriacao(LocalDateTime.now());
		shopRepository.save(shop);
		return "Opreação Concluida com suceso";
	}
	@Transactional
	@CacheEvict(value = "shop", allEntries = true)
	public String update(Shop shop) {
		if (!existsByIdShop(shop.getId())) {
			throw new OperationNotAllowedException("Loja não encontrada para atualização.");

		}
		shop.setDataAtualizacao(LocalDateTime.now());
		shopRepository.saveAndFlush(shop);
		return "Operação realizada com Sucesso";
	}
	@Cacheable(value = "shop", key = "'list'")
	public List<ShopDTO> list(){
		return shopRepository.findAll().stream().map(this:: mapToShopDTO).collect(Collectors.toList());
	}
	
	@Cacheable(value = "shop", key = "'listComboBox'")
    public List<ShopDTOs> listCompobox(){
		return shopRepository.findAll().stream().map(this:: mapToShopDTOs).collect(Collectors.toList());

    }
	
	public List<ShopDTO> search(String nome) {

		return shopRepository.busca(nome).stream().map(this:: mapToShopDTO).collect(Collectors.toList());
	}

	// Método para verificar se nome ou número de contribuinte já existem
	private boolean existsShop(String nome, String numeroContribuite) {
		return shopRepository.existsByNome(nome) || shopRepository.existsByNumeroContribuite(numeroContribuite);
	}

	// Metudo para verificar id da loja
	private boolean existsByIdShop(Long id) {
		return shopRepository.existsById(id);
	}
	
	private ShopDTO mapToShopDTO(Shop shop) {
		ShopDTO shopDTO = new ShopDTO();
		shopDTO.setId(shop.getId());
		shopDTO.setNome(shop.getNome());
		shopDTO.setCaixaPostal(shop.getCaixaPostal());
		shopDTO.setContacto(shop.getContacto());
		shopDTO.setEmail(shop.getEmail());
		shopDTO.setNumeroContribuite(shop.getNumeroContribuite());
		shopDTO.setIdlocation(shop.getLocation().getId());
		shopDTO.setNomelocation(shop.getLocation().getNome());
		shopDTO.setDatacriacao(shop.getDatacriacao());
		shopDTO.setDataAtualizacao(shop.getDataAtualizacao());
		return shopDTO;
	}
	private ShopDTOs mapToShopDTOs(Shop shop) {
		ShopDTOs shopDTOs = new ShopDTOs();
		shopDTOs.setId(shop.getId());
		shopDTOs.setNome(shop.getNome());
		return shopDTOs;
	}
}
