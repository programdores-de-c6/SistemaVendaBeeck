package com.ideias_inovadora.model;

public enum TipoTrasacao {
	VENDA("VENDA"), REQUISICAO("REQUISICAO"), DEVOLUCAO("DEVOLUCAO");

	private String descricao;

	TipoTrasacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
