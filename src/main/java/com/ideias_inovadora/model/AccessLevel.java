package com.ideias_inovadora.model;

public enum AccessLevel {
	ROLE_ADMIN ("ROLE_ADMIN"), // Administrador - acesso total ao sistema
	ROLE_MANAGER ("ROLE_MANAGER"), // Gerente - acesso a funcionalidades avançadas
	ROLE_USER ("ROLE_USER"), // Usuário - acesso às funções básicas do sistema
	NO_ACCESS ("NO_ACCESS"); // Sem acesso ao sistema
	
	private String descricao;
	
	private AccessLevel(String descricao) {
this.descricao=descricao;
}

	public synchronized String getDescricao() {
		return descricao;
	}

}
