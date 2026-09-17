package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideias_inovadora.config.STNCurrencySerializer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobTitle")
public class JobTitle implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nomecargo;
	private String descricao;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccessLevel accessLevel = AccessLevel.NO_ACCESS;
	@JsonSerialize(using = STNCurrencySerializer.class)
	private BigDecimal valor;
	@JsonFormat(pattern = "dd/MM/yyy HH:mm")
	private LocalDateTime datacricao;
	private LocalDateTime dataActualizacao;

	public JobTitle() {
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

	public String getNomecargo() {
		return nomecargo;
	}

	public void setNomecargo(String nomecargo) {
		this.nomecargo = nomecargo;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

}
