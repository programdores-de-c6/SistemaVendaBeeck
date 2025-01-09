package com.ideias_inovadora.model;

public enum StatusCaixa {
	ABERTO("ABERTO"), FECHADO("FECHADO");

	private String descricao;

	StatusCaixa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
