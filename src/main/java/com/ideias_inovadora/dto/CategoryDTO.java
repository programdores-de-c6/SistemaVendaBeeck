package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String nome;
	private String imposto;
	private BigDecimal valor;

	public CategoryDTO() {
		super();
	}

	public synchronized long getId() {
		return id;
	}

	public synchronized void setId(long id) {
		this.id = id;
	}

	public synchronized String getNome() {
		return nome;
	}

	public synchronized void setNome(String nome) {
		this.nome = nome;
	}

	public synchronized String getImposto() {
		return imposto;
	}

	public synchronized void setImposto(String imposto) {
		this.imposto = imposto;
	}

	public synchronized BigDecimal getValor() {
		return valor;
	}

	public synchronized void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
