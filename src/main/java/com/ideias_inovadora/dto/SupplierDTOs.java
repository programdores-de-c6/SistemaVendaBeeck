package com.ideias_inovadora.dto;

import java.io.Serializable;

import com.ideias_inovadora.model.Country;

public class SupplierDTOs implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String nome;
	private String contactoPrincipal;
	private String contactoSecudario;
	private String email;
	private String numeroContribuite;
	private String nomepais;
	private long idpais;
	private Country country;
	public SupplierDTOs() {
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
	public synchronized String getContactoPrincipal() {
		return contactoPrincipal;
	}
	public synchronized void setContactoPrincipal(String contactoPrincipal) {
		this.contactoPrincipal = contactoPrincipal;
	}
	public synchronized String getContactoSecudario() {
		return contactoSecudario;
	}
	public synchronized void setContactoSecudario(String contactoSecudario) {
		this.contactoSecudario = contactoSecudario;
	}
	public synchronized String getEmail() {
		return email;
	}
	public synchronized void setEmail(String email) {
		this.email = email;
	}
	public synchronized String getNumeroContribuite() {
		return numeroContribuite;
	}
	public synchronized void setNumeroContribuite(String numeroContribuite) {
		this.numeroContribuite = numeroContribuite;
	}
	public synchronized Country getCountry() {
		return country;
	}
	public synchronized void setCountry(Country country) {
		this.country = country;
	}
	public synchronized String getNomepais() {
		return nomepais;
	}
	public synchronized void setNomepais(String nomepais) {
		this.nomepais = nomepais;
	}
	public synchronized long getIdpais() {
		return idpais;
	}
	public synchronized void setIdpais(long idpais) {
		this.idpais = idpais;
	}
	

}
