package com.ideias_inovadora.model;

public enum CategoriaDespesa {
	IMPOSTO("IMPOSTO"), SALARIO("SALARIO"),BONUS("BONUS"), COMPRA("COMPRA"), OUTROS("OUTROS");
 
	
	private String descricao;

	CategoriaDespesa(String descricao){
		this.descricao=descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	
	
}
