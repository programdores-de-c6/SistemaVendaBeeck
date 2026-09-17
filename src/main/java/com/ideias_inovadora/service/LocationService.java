package com.ideias_inovadora.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.LocationDTO;
import com.ideias_inovadora.dto.LocationDTOs;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.Location;
import com.ideias_inovadora.repository.CustomerRepository;
import com.ideias_inovadora.repository.EmployeeRepository;
import com.ideias_inovadora.repository.LocationRepository;
import com.ideias_inovadora.repository.ShopRepository;

import jakarta.transaction.Transactional;

@Service
public class LocationService {

	@Autowired
	LocationRepository locationRepository;
	@Autowired
	EmployeeRepository funcionarioRepository;
	@Autowired
	ShopRepository shopRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ModelMapper modelMapper;

	@Transactional
	@CacheEvict(value = "location", allEntries = true)
	public String create(Location location) {

	    if (location.getDistrict() == null) {
	        throw new IllegalArgumentException(
	            "O distrito é obrigatório."
	        );
	    }

	    location.setDatacriacao(LocalDateTime.now());

	    locationRepository.save(location);

	    return "Salvo com sucesso";
	}
	@Transactional
	@CacheEvict(value = "location", allEntries = true)
	public String update(Location location) {

		if (!locationRepository.existsById(location.getId())) {
			throw new OperationNotAllowedException("Erro na localidade");
		}
		location.setDataAtualizacao(LocalDateTime.now());
		locationRepository.saveAndFlush(location);
		return "Actualizado com Sucesso";
	}
	@Cacheable(value = "location", key = "'list'")
	public List<LocationDTO> list() {
		return locationRepository.findAll().stream().map(this::mapToLocationDTO).collect(Collectors.toList());

	}
	@Cacheable(value = "location", key = "'listComboBox'")
     public List<LocationDTOs> listCompobox(){
 		return locationRepository.findAll().stream().map(this::mapToLocationDTOs).collect(Collectors.toList());

     }
	public List<LocationDTO> search(String nome) {

		return locationRepository.findByNomeContainingIgnoreCase(nome).stream().map(this::mapToLocationDTO).collect(Collectors.toList());
	}
	@CacheEvict(value = "location", allEntries = true)
	public void delete(long id) {

		// Verifica vínculos com outras entidades
		List<String> entidadesVinculadas = new ArrayList<>();

		if (customerRepository.existsByLocationId(id)) {
			entidadesVinculadas.add("clientes");
		}
		if (funcionarioRepository.existsByLocationId(id)) {
			entidadesVinculadas.add("funcionários");
		}
		if (shopRepository.existsByLocationId(id)) {
			entidadesVinculadas.add("lojas");
		}

		// Se houver vínculos, lança exceção com detalhes
		if (!entidadesVinculadas.isEmpty()) {
			String mensagem = String.format("Localidade não pode ser excluída: vinculada a %s.",
					String.join(", ", entidadesVinculadas));
			throw new OperationNotAllowedException(mensagem);
		}

		locationRepository.deleteById(id);
	}

	private LocationDTO mapToLocationDTO(Location location) {
		LocationDTO locationDTO = new LocationDTO();
		locationDTO.setId(location.getId());
		locationDTO.setNome(location.getNome());
		locationDTO.setSigla(location.getSigla());
		locationDTO.setNomedistrito(location.getDistrict().getNome());
		locationDTO.setDistritoid(location.getDistrict().getId());
		locationDTO.setDatacriacao(location.getDatacriacao());
		locationDTO.setDataAtualizacao(location.getDataAtualizacao());
		return locationDTO;
	}
	private LocationDTOs mapToLocationDTOs(Location location) {
		LocationDTOs locationDTO = new LocationDTOs();
		locationDTO.setId(location.getId());
		locationDTO.setNome(location.getNome());
		return locationDTO;
	}
}
