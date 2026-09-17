package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideias_inovadora.model.Location;
import com.ideias_inovadora.model.ShopType;



public class ShopDTO implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String nome;
	private String numeroContribuite;
	private String email;
	private String contacto;
	private ShopType shopType;
	private String shopTypes;
	private int caixaPostal;
	@JsonFormat(pattern = "dd/MM/yyy HH:mm")
	private LocalDateTime datacriacao;
	@JsonFormat(pattern = "dd/MM/yyy HH:mm")
	private LocalDateTime dataAtualizacao;
	private String nomelocation;
	private long idlocation;
	private MultipartFile file;
	Location location;
	private String logoUrl;
	
	
	public ShopDTO() {
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
	public String getNumeroContribuite() {
		return numeroContribuite;
	}
	public void setNumeroContribuite(String numeroContribuite) {
		this.numeroContribuite = numeroContribuite;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public int getCaixaPostal() {
		return caixaPostal;
	}
	public void setCaixaPostal(int caixaPostal) {
		this.caixaPostal = caixaPostal;
	}
	public LocalDateTime getDatacriacao() {
		return datacriacao;
	}
	public void setDatacriacao(LocalDateTime datacriacao) {
		this.datacriacao = datacriacao;
	}
	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}
	
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public String getNomelocation() {
		return nomelocation;
	}
	public void setNomelocation(String nomelocation) {
		this.nomelocation = nomelocation;
	}
	public Long getIdlocation() {
		return idlocation;
	}
	public void setIdlocation(Long idlocation) {
		this.idlocation = idlocation;
	}
	public synchronized MultipartFile getFile() {
		return file;
	}
	public synchronized void setFile(MultipartFile file) {
		this.file = file;
	}
	public synchronized void setIdlocation(long idlocation) {
		this.idlocation = idlocation;
	}
	public synchronized Location getLocation() {
		return location;
	}
	public synchronized void setLocation(Location location) {
		this.location = location;
	}
	public ShopType getShopType() {
		return shopType;
	}
	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}
	public String getShopTypes() {
		return shopTypes;
	}
	public void setShopTypes(String shopTypes) {
		this.shopTypes = shopTypes;
	}
	


}
