package com.ideias_inovadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario")
public class Funcionario extends Pessoa {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "loja_fk")
	private Loja loja;
	@ManyToOne
	@JoinColumn(name = "nivelAcesso_fk")
	private NivelAcesso nivelAcesso;
	@ManyToOne
	@JoinColumn(name = "localidade_fk")
	private Localidade localidade;
	@Enumerated(EnumType.STRING)
	private Status status;
	@ManyToOne
	@JoinColumn(name = "salarioBase_fk")
	private SalarioBase salarioBase;
	public Funcionario() {
		super();
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	public NivelAcesso getNivelAcesso() {
		return nivelAcesso;
	}

	public void setNivelAcesso(NivelAcesso nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SalarioBase getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(SalarioBase salarioBase) {
		this.salarioBase = salarioBase;
	}

}
