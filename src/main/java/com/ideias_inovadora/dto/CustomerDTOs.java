package com.ideias_inovadora.dto;

import java.io.Serializable;

public class CustomerDTOs implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long idLocations;
	private String nome;
	private String numeroContribuinte;
	private String contactoPrincipal;
	private String contactoSecudario;
	private String email;
	private String nomeLocations;
	
	
	
	
	
	public CustomerDTOs() {
		super();
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdLocations() {
		return idLocations;
	}
	public void setIdLocations(long idLocations) {
		this.idLocations = idLocations;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNumeroContribuinte() {
		return numeroContribuinte;
	}
	public void setNumeroContribuinte(String numeroContribuinte) {
		this.numeroContribuinte = numeroContribuinte;
	}
	public String getContactoPrincipal() {
		return contactoPrincipal;
	}
	public void setContactoPrincipal(String contactoPrincipal) {
		this.contactoPrincipal = contactoPrincipal;
	}
	public String getContactoSecudario() {
		return contactoSecudario;
	}
	public void setContactoSecudario(String contectoSecudario) {
		this.contactoSecudario = contectoSecudario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNomeLocations() {
		return nomeLocations;
	}
	public void setNomeLocations(String nomeLocations) {
		this.nomeLocations = nomeLocations;
	}
	

}
