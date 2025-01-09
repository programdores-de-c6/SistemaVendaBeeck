package com.ideias_inovadora.model;

public enum Status {
	ACTIVO("ACTIVO"), ÍNATIVO("ÍNATIVO");

	private String descricao;

	Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
