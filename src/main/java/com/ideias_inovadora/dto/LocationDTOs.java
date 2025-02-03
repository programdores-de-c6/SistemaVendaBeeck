package com.ideias_inovadora.dto;

import java.io.Serializable;



public class LocationDTOs implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	private long id;
	private String nome;

	
	
	public LocationDTOs() {
		super();
	}
	public LocationDTOs(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
		
		
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	

}
