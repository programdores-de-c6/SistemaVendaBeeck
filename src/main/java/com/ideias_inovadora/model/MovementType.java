package com.ideias_inovadora.model;

public enum TipoMovimento {
	ENTRADA("ENTRADA"), SAÍDA("SAÍDA");

	private String descricao;

	TipoMovimento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
