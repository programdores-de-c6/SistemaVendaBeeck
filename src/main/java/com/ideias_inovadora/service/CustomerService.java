package com.ideias_inovadora.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.CustomerDTOs;
import com.ideias_inovadora.dto.SupplierDTOs;
import com.ideias_inovadora.model.Customer;
import com.ideias_inovadora.model.Supplier;
import com.ideias_inovadora.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Transactional
	@CacheEvict(value = "customer", allEntries = true)
	public String create(Customer customer) {
	    customer.setDatacriacao(LocalDateTime.now());
	    customerRepository.save(customer);

	    return "Operação Realizada com Sucesso";
	}

	@Transactional
	@CacheEvict(value = "customer", allEntries = true)
	public String update(Customer customer) {
	    customer.setDataAtualizacao(LocalDateTime.now());
	    customerRepository.saveAndFlush(customer);

	    return "Operação Realizada com Sucesso";
	}

	@Cacheable(value = "customer", key = "'list'")
	public List<CustomerDTOs> list() {
	    return customerRepository.findAll()
	            .stream()
	            .map(this::mapToCustomerDTOs)
	            .collect(Collectors.toList());
	}

	private CustomerDTOs mapToCustomerDTOs(Customer customer) {

	    CustomerDTOs customerDTOs = new CustomerDTOs();

	    customerDTOs.setId(
	        customer.getId()
	    );

	    customerDTOs.setNome(
	        customer.getNome()
	    );

	    customerDTOs.setContactoPrincipal(
	        customer.getContactoPrincipal()
	    );

	    customerDTOs.setContactoSecudario(
	        customer.getContactoSecudario()
	    );

	    customerDTOs.setEmail(
	        customer.getEmail()
	    );

	    customerDTOs.setNumeroContribuinte(
	        customer.getNumeroContribuinte()
	    );

	    // ====================================================
	    // LOCALIDADE
	    // ====================================================

	    if (customer.getLocation() != null) {

	        customerDTOs.setNomeLocations(
	            customer.getLocation().getNome()
	        );

	        customerDTOs.setIdLocations(
	            customer.getLocation().getId()
	        );

	    } else {

	        // Cliente sem localidade associada
	        customerDTOs.setNomeLocations(
	            null
	        );

	        customerDTOs.setIdLocations(
	            0
	        );
	    }

	    return customerDTOs;
	}

	

	
	@Transactional
	public Map<String, Object> processarClienteParaVenda(Long customerId, String nomeFront, String nifFront) {
	    Map<String, Object> resultado = new HashMap<>();
	    
	    // 1. Definição de valores padrão para busca
	    String nif = (nifFront != null && !nifFront.isBlank()) ? nifFront : "999999999";
	    String nome = (nomeFront != null && !nomeFront.isBlank()) ? nomeFront : "Venda ao Público";

	    // CENÁRIO 1: O utilizador selecionou um cliente existente (pelo ID)
	    if (customerId != null) {
	        Customer c = customerRepository.findById(customerId)
	                .orElseThrow(() -> new RuntimeException("Cliente ID " + customerId + " não encontrado."));
	        resultado.put("customer", c);
	        resultado.put("nomeInformal", null); // Não precisa de nome informal se o cliente é real
	        return resultado;
	    }

	    // CENÁRIO 2: Registo Rápido (NIF Real, ex: 123456789)
	    if (!"999999999".equals(nif)) {
	        Customer c = customerRepository.findByNumeroContribuinte(nif)
	                .orElseGet(() -> {
	                    // Se não existe, cria o registro real para fidelizar
	                    Customer newC = new Customer();
	                    newC.setNome(nome);
	                    newC.setNumeroContribuinte(nif);
	                    newC.setDatacriacao(LocalDateTime.now());
	                    return customerRepository.save(newC);
	                });
	        resultado.put("customer", c);
	        resultado.put("nomeInformal", null);
	        return resultado;
	    }

	    // CENÁRIO 3 e 4: Nome Informal ou Anónimo (NIF é 999999999)
	    Customer consumidorFinal = customerRepository.findByNumeroContribuinte("999999999")
	            .orElseThrow(() -> new RuntimeException("Erro Crítico: Cliente 999999999 não existe no banco."));

	    resultado.put("customer", consumidorFinal);
	    // ✅ Se o vendedor escreveu algo, gravamos na Sale. Se não, fica "Venda ao Público"
	    resultado.put("nomeInformal", nome); 

	    return resultado;
	}
}