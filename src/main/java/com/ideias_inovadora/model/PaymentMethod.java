package com.ideias_inovadora.model;

public enum PaymentMethod  { //MetodoPagamento
	CARTÃO("CARTÃO"), DINHEIRO("DINHEIRO"),TRANSFERÊNCIA("TRANSFERÊNCIA");
 
	
	private String descricao;

	PaymentMethod (String descricao){
		this.descricao=descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	
	
}
