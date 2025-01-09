package com.ideias_inovadora.model;

public enum StatusPresenca {
	PRESENTE("PRESENTE"), AUSENTE("AUSENTE");

	private String descricao;

	StatusPresenca(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
