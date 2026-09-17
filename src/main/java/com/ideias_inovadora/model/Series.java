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
@Table(name = "series")
public class Series implements Serializable {
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
	@JoinColumn(name = "employee_fk")
	private Employee employee;
	private LocalDateTime datacriacao;
	private LocalDateTime dataAutorizacao;

	public Series() {
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

	

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
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
