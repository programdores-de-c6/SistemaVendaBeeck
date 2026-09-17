package com.ideias_inovadora.model;

public enum ExpenseCategory { //CategoriaDespesa
	IMPOSTO("IMPOSTO"), SALARIO("SALARIO"),BONUS("BONUS"), COMPRA("COMPRA"), OUTROS("OUTROS");
 
	
	private String descricao;

	ExpenseCategory (String descricao){
		this.descricao=descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	
	
}
