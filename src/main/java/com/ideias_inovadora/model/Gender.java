package com.ideias_inovadora.model;

public enum Gender {
	M("Masculino"), F("Feminino");

	private String descricao;

	Gender (String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
