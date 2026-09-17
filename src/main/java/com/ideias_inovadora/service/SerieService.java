package com.ideias_inovadora.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.SeriesDTOs;
import com.ideias_inovadora.model.Series;
import com.ideias_inovadora.repository.SerieRepository;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
@Service
public class SerieService {

	@Autowired
	SerieRepository serieRepository;

	public String create(Series series) {
		
		
		
		
		
		

		series.setDatacriacao(LocalDateTime.now());
		serieRepository.save(series);
		return "Opreação Concluida com suceso";
	}

	public String update(Series series) {

		series.setDataAutorizacao(LocalDateTime.now());
		serieRepository.saveAndFlush(series);
		return "Opreação Concluida com suceso";
	}

	public List<SeriesDTOs> list() {

		return serieRepository.findAll().stream().map(this::mapToSeriesDTO).collect(Collectors.toList());

	}

	private SeriesDTOs mapToSeriesDTO(Series series) {
		SeriesDTOs seriesDTOs = new SeriesDTOs();
		seriesDTOs.setId(series.getId());
		seriesDTOs.setSerie(series.getSerie());
		seriesDTOs.setNumeroAutorizacao(series.getNumeroAutorizacao());
		seriesDTOs.setAno(series.getAno());
		seriesDTOs.setShop(series.getShop().getNome());
		seriesDTOs.setShops(series.getShop().getId());

		return seriesDTOs;
	}
	
	
	/**
	 * Gera o próximo número de fatura de forma sequencial e segura.
	
	 */
	@Transactional
	public Map<String, Object> gerarProximoNumeroFactura(long shopId) {
		int anoActual = LocalDateTime.now().getYear();
		
		// 1. Busca a série da loja para o ano corrente
		Series serieConfig = serieRepository.findByShopIdAndAno(shopId, anoActual)
				.orElseThrow(() -> new RuntimeException("Série não configurada para o ano " + anoActual + " nesta loja."));

		// 2. Incrementa a sequência
		int novaSequencia = serieConfig.getUltimasequecia() + 1;
		serieConfig.setUltimasequecia(novaSequencia);
		
		// 3. Salva a atualização no banco imediatamente
		serieRepository.save(serieConfig);

		// 4. Formata o número (Série + / + 7 dígitos com zeros à esquerda)
		// %07d significa: número inteiro, com 7 dígitos, preenchido com zeros
		String numeroFormatado = String.format("%07d", novaSequencia);
		String numeroFactura= serieConfig.getSerie() + "/" + numeroFormatado;
		
		Map<String, Object> resultado = new HashMap<>();
	    resultado.put("numeroFactura", numeroFactura);
	    resultado.put("serieObject", serieConfig);
		return resultado;
	}

}
