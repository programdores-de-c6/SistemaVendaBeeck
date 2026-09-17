package com.ideias_inovadora.dto;

import java.io.Serializable;

public class SeriesDTOs implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private long id;
	private int ano;
	private int ultimasequecia;
	private String serie;
	private String numeroAutorizacao;
	private String shop;
	private long shops;
	
	
	
	
	
	public SeriesDTOs() {
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
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public long getShops() {
		return shops;
	}
	public void setShops(long shops) {
		this.shops = shops;
	}

	
	
}
