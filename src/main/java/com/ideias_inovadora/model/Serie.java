package com.ideias_inovadora.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "serie")
public class Serie implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int ano;
	private int ultimasequecia;
	private String serie;
	private String numeroAutorizacao;
	@ManyToOne
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	@ManyToOne
	@JoinColumn(name = "funcionario_fk")
	private Funcionario funcionario;
	private LocalDateTime datacriacao;
	private LocalDateTime dataAutorizacao;

	public Serie() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getUltimasequecia() {
		return ultimasequecia;
	}

	public void setUltimasequecia(int ultimasequecia) {
		this.ultimasequecia = ultimasequecia;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumeroAutorizacao() {
		return numeroAutorizacao;
	}

	public void setNumeroAutorizacao(String numeroAutorizacao) {
		this.numeroAutorizacao = numeroAutorizacao;
	}

	

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public LocalDateTime getDatacriacao() {
		return datacriacao;
	}

	public void setDatacriacao(LocalDateTime datacriacao) {
		this.datacriacao = datacriacao;
	}

	public LocalDateTime getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(LocalDateTime dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

}
