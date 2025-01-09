package com.ideias_inovadora.model;

public enum StatusRequisicao {
	CONCLUIDO("CONCLUIDO"), PEDENTE("PEDENTE");
	
	private String descricao;
	
	 StatusRequisicao(String descricao) {
		this.descricao=descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	
	

}
