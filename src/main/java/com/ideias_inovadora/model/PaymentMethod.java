package com.ideias_inovadora.model;

public enum MetodoPagamento {
	CARTÃO("CARTÃO"), DINHEIRO("DINHEIRO"),TRANSFERÊNCIA("TRANSFERÊNCIA");
 
	
	private String descricao;

	MetodoPagamento(String descricao){
		this.descricao=descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	
	
}
