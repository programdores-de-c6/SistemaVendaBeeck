package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideias_inovadora.config.STNCurrencySerializer;
import com.ideias_inovadora.model.AccessLevel;
import com.ideias_inovadora.model.Gender;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.Status;



public class EmployeeDTOs implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private long id;
	private String nome;
	@JsonFormat(pattern = "dd/MM/yyy")
	private LocalDate dataNascimento;
	@JsonFormat(pattern = "dd/MM/yyy")
	private LocalDate dataAdmissao;
	private String contactoPrincipal;
	private String contactoSecudario;
	private String email;
	private Gender  gender;
	private String numeroBi;
	private String numeroContribuinte;
	private AccessLevel accessLevel; 
	private long shop;
	private String shops;
	private Shop shop2;
	private String jobTitle;
	private long jobtitle;
	@JsonSerialize(using = STNCurrencySerializer.class)
	private BigDecimal salaaryBases;
	private long location ;
	private String locations;
	private Status status;
	private String logoUrl;
	public EmployeeDTOs() {
		super();
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

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
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
	public void setContactoSecudario(String contactoSecudario) {
		this.contactoSecudario = contactoSecudario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getNumeroBi() {
		return numeroBi;
	}
	public void setNumeroBi(String numeroBi) {
		this.numeroBi = numeroBi;
	}
	public String getNumeroContribuinte() {
		return numeroContribuinte;
	}
	public void setNumeroContribuinte(String numeroContribuinte) {
		this.numeroContribuinte = numeroContribuinte;
	}
	public AccessLevel getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}
	public long getShop() {
		return shop;
	}
	public void setShop(long shop) {
		this.shop = shop;
	}
	public String getShops() {
		return shops;
	}
	public void setShops(String shops) {
		this.shops = shops;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public long getJobtitle() {
		return jobtitle;
	}
	public void setJobtitle(long jobtitle) {
		this.jobtitle = jobtitle;
	}
	public BigDecimal getSalaaryBases() {
		return salaaryBases;
	}
	public void setSalaaryBases(BigDecimal salaaryBases) {
		this.salaaryBases = salaaryBases;
	}
	public long getLocation() {
		return location;
	}
	public void setLocation(long location) {
		this.location = location;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public Shop getShop2() {
		return shop2;
	}
	public void setShop2(Shop shop2) {
		this.shop2 = shop2;
	}
	

}
