package com.ideias_inovadora.dto;

import java.io.Serializable;

public class EmployeeSenhaDTO implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private long id;
	private String senha;
	private String newSenha;
	private String email;
	private String codigo;
	
	public EmployeeSenhaDTO() {
		super();
	}
	public synchronized long getId() {
		return id;
	}
	public synchronized void setId(long id) {
		this.id = id;
	}
	public synchronized String getSenha() {
		return senha;
	}
	public synchronized void setSenha(String senha) {
		this.senha = senha;
	}
	public synchronized String getNewSenha() {
		return newSenha;
	}
	public synchronized void setNewSenha(String newSenha) {
		this.newSenha = newSenha;
	}
	public synchronized String getEmail() {
		return email;
	}
	public synchronized void setEmail(String email) {
		this.email = email;
	}
	public synchronized String getCodigo() {
		return codigo;
	}
	public synchronized void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
