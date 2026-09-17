package com.ideias_inovadora.model;

public enum ShopType {
	LOJA("LOJA"), ARMAZEM("ARMAZEM"), GRAFICA("GRÁFICA");;

	private String descricao;

	ShopType(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
