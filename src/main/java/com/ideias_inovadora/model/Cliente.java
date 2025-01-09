package com.ideias_inovadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente extends Pessoa {

	private static final long serialVersionUID = 1L;

	private String codigocliente;
	@ManyToOne
	@JoinColumn(name = "localidade_fk")
	private Localidade localidade;
	
	public Cliente() {
		super();
	}

	public Cliente(String codigocliente) {
		super();
		this.codigocliente = codigocliente;
	}

	public String getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(String codigocliente) {
		this.codigocliente = codigocliente;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

}
