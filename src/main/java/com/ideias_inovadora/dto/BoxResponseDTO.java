package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ideias_inovadora.model.Box;

public class BoxResponseDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	

	    private long id;
	    private BigDecimal valorInicial;
	    private BigDecimal valorFinal;
		private BigDecimal valorDia;
	    private String statusCaixa;
	    private LocalDateTime dataAbertura;
	    private LocalDateTime dataFecho;
	    
	    // Nomes diretos para o PDF não ter de navegar em objetos complexos
	    private String nomeOperadorAbertura;
	    private String nomeOperadorFecho;
	    
	    // Dados da Loja para o cabeçalho do recibo
	    private String shopNome;
	    private String shopNif;
	    private String shopEndereco;

	   

		public BoxResponseDTO() {
			super();
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public BigDecimal getValorInicial() {
			return valorInicial;
		}

		public void setValorInicial(BigDecimal valorInicial) {
			this.valorInicial = valorInicial;
		}

		public BigDecimal getValorFinal() {
			return valorFinal;
		}

		public void setValorFinal(BigDecimal valorFinal) {
			this.valorFinal = valorFinal;
		}

		public BigDecimal getValorDia() {
			return valorDia;
		}

		public void setValorDia(BigDecimal valorDia) {
			this.valorDia = valorDia;
		}

		public String getStatusCaixa() {
			return statusCaixa;
		}

		public void setStatusCaixa(String statusCaixa) {
			this.statusCaixa = statusCaixa;
		}

		public LocalDateTime getDataAbertura() {
			return dataAbertura;
		}

		public void setDataAbertura(LocalDateTime dataAbertura) {
			this.dataAbertura = dataAbertura;
		}

		public LocalDateTime getDataFecho() {
			return dataFecho;
		}

		public void setDataFecho(LocalDateTime dataFecho) {
			this.dataFecho = dataFecho;
		}

		public String getNomeOperadorAbertura() {
			return nomeOperadorAbertura;
		}

		public void setNomeOperadorAbertura(String nomeOperadorAbertura) {
			this.nomeOperadorAbertura = nomeOperadorAbertura;
		}

		public String getNomeOperadorFecho() {
			return nomeOperadorFecho;
		}

		public void setNomeOperadorFecho(String nomeOperadorFecho) {
			this.nomeOperadorFecho = nomeOperadorFecho;
		}

		public String getShopNome() {
			return shopNome;
		}

		public void setShopNome(String shopNome) {
			this.shopNome = shopNome;
		}

		public String getShopNif() {
			return shopNif;
		}

		public void setShopNif(String shopNif) {
			this.shopNif = shopNif;
		}

		public String getShopEndereco() {
			return shopEndereco;
		}

		public void setShopEndereco(String shopEndereco) {
			this.shopEndereco = shopEndereco;
		}
	  
	
}
