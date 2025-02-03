package com.ideias_inovadora.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ideias_inovadora.model.Distrito;
import com.ideias_inovadora.model.Pais;
import com.ideias_inovadora.repository.DistritoRepository;
import com.ideias_inovadora.repository.PaisRepository;

import jakarta.transaction.Transactional;

@Component
public class DataLoader implements CommandLineRunner {
	private final PaisRepository paisRepository;
	private final DistritoRepository distritoRepository;
	private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

	public DataLoader(PaisRepository paisRepository, DistritoRepository distritoRepository) {
		this.paisRepository = paisRepository;
		this.distritoRepository = distritoRepository;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		try {
			carregarDados();
		} catch (Exception e) {
			logger.error("Erro ao carregar dados iniciais", e);
			throw e;
		}
	}

private void carregarDados() {
		// criar e salvar lista de pais
		if (paisRepository.count() == 0) {
			List<Pais> paises = List.of(new Pais("SÃO TOMÉ E PRINCÍPE", "STP", LocalDateTime.now()),
					new Pais("BRASIL", "BR", LocalDateTime.now()), new Pais("PORTUGAL", "PT", LocalDateTime.now()),
					new Pais("ANGOLA", "AO", LocalDateTime.now()), new Pais("MOÇAMBIQUE", "MZ", LocalDateTime.now())

			);
			paisRepository.saveAll(paises);	
			logger.info("Países cadastrados com sucesso!");
		}
		// Buscar os países já cadastrados
		Map<String, Pais> paisMap = new HashMap<>();
		paisRepository.findAll().forEach(pais -> paisMap.put(pais.getNome(), pais));

		// Criar e salvar os distritos associados aos países
		if (distritoRepository.count() == 0) {
			List<Distrito> distritos = List.of(
					new Distrito("ÁGUA GRANDE", "AG", paisMap.get("SÃO TOMÉ E PRINCÍPE"), LocalDateTime.now()),
					new Distrito("MÉ-ZÓCHI", "MZ", paisMap.get("SÃO TOMÉ E PRINCÍPE"), LocalDateTime.now()),
					new Distrito("CANTAGALO", "CT", paisMap.get("SÃO TOMÉ E PRINCÍPE"), LocalDateTime.now()),
					new Distrito("LOBATA", "LB", paisMap.get("SÃO TOMÉ E PRINCÍPE"), LocalDateTime.now()),
					new Distrito("CAUÉ", "CE", paisMap.get("SÃO TOMÉ E PRINCÍPE"), LocalDateTime.now()),
					new Distrito("LEMBÁ", "LB", paisMap.get("SÃO TOMÉ E PRINCÍPE"), LocalDateTime.now()),
					new Distrito("REGIÃO AUTONOMA DO PRINCIPE", "RAP", paisMap.get("SÃO TOMÉ E PRINCÍPE"),
							LocalDateTime.now())
			);
			distritoRepository.saveAll(distritos);
			logger.info("Distritos cadastrados com sucesso!");
		}
	}

}
