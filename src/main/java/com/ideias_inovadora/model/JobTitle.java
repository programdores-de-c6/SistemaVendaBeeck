package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "baseSalary")
public class BaseSalary implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String descricao;
	private BigDecimal valor;
	private LocalDateTime datacricao;
	private LocalDateTime dataActualizacao;

	public BaseSalary() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDateTime getDatacricao() {
		return datacricao;
	}

	public void setDatacricao(LocalDateTime datacricao) {
		this.datacricao = datacricao;
	}

	public LocalDateTime getDataActualizacao() {
		return dataActualizacao;
	}

	public void setDataActualizacao(LocalDateTime dataActualizacao) {
		this.dataActualizacao = dataActualizacao;
	}

}
