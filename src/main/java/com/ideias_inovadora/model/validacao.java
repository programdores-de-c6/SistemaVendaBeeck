package com.ideias_inovadora.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "validacao")
public class validacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String codigo;
	private boolean estado;
	private String senhatemporaria;
	private LocalDate dataespiracao;
	private boolean usado;
	
	public validacao() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getSenhatemporaria() {
		return senhatemporaria;
	}

	public void setSenhatemporaria(String senhatemporaria) {
		this.senhatemporaria = senhatemporaria;
	}

	public LocalDate getDataespiracao() {
		return dataespiracao;
	}

	public void setDataespiracao(LocalDate dataespiracao) {
		this.dataespiracao = dataespiracao;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

}
