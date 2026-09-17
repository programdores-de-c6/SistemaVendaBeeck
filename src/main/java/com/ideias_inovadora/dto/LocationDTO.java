package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;


public class LocationDTO implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	private long id;
	private String nome;
	private String sigla;
	@NotNull
	private long distritoid;
	private String nomedistrito;
	@JsonFormat(pattern = "dd/MM/yyy HH:mm")
	private LocalDateTime datacriacao;
	@JsonFormat(pattern = "dd/MM/yyy HH:mm")
	private LocalDateTime dataAtualizacao;
	
	public LocationDTO() {
		super();
	}
	public LocationDTO(long id, String nome,String sigla, long distritoid, String nomedistrito, LocalDateTime datacriacao,
			LocalDateTime dataAtualizacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.sigla = sigla;
		this.distritoid = distritoid;
		this.nomedistrito = nomedistrito;
		this.datacriacao = datacriacao;
		this.dataAtualizacao = dataAtualizacao;
		
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
	public long getDistritoid() {
		return distritoid;
	}
	public void setDistritoid(long distritoid) {
		this.distritoid = distritoid;
	}
	public String getNomedistrito() {
		return nomedistrito;
	}
	public void setNomedistrito(String nomedistrito) {
		this.nomedistrito = nomedistrito;
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
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	
	

}
