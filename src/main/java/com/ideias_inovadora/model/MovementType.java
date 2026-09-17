package com.ideias_inovadora.model;

public enum MovementType   {//TipoMovimentoTipoMovimento
	ENTRADA("ENTRADA"), SAÍDA("SAÍDA"), TRANSFERENCIA("TRANSFERENCIA"), AJUSTE("AJUSTE");

	private String descricao;

	MovementType (String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
