package com.ideias_inovadora.model;

public enum TransactionType  { //TipoTrasacao
	VENDA("VENDA"), REQUISICAO("REQUISICAO"), DEVOLUCAO("DEVOLUCAO");

	private String descricao;

	TransactionType (String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
