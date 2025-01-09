package com.ideias_inovadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fornecedor")
public class Fornecedor extends Pessoa {
	
	private static final long serialVersionUID = 1L;
  
	@ManyToOne
	@JoinColumn(name = "pais_fk")
	private Pais pais;

	public Fornecedor() {
		super();
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	
	
}
