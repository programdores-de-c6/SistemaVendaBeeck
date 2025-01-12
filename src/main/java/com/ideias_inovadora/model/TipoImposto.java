package com.ideias_inovadora.model;

public enum TipoImposto {
	IVA("IVA"), SEGURAÇA_SOCIAL("SEGURAÇA_SOCIAL"), IRS("IRS"), OUTROS("OUTROS");

	private String descricao;

	TipoImposto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
