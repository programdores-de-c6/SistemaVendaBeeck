package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.ideias_inovadora.model.Gender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class EmployeeDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private long id;
	@NotBlank(message = "O nome é obrigatório.")
	private String nome;
	@NotNull(message = "Data Nascimento obrigatório.")
	private LocalDate dataNascimento;
	@NotNull(message = "Data Admissão obrigatório.")
	private LocalDate dataAdmissao;
	@NotBlank(message = "Contacto Principal obrigatório.")
	//@Pattern(regexp = "\\d{7}", message = "O Número de Contacto Principal deve ter exactamente 9 dígitos.")
	private String contactoPrincipal;
	private String contactoSecudario;
	@NotBlank(message = "O e-mail é obrigatório.")
	@Email(message = "O formato do e-mail é inválido.")
	private String email;
	private String senha;
    @Enumerated(EnumType.STRING)
	private Gender  gender;
	private String numeroBi;
	@NotBlank(message = "Número de Cartão Contribuinte  obrigatório.")
	//@Pattern(regexp = "\\d{9}", message = "O Número de Contribuinte deve ter exactamente 9 dígitos.")
	private String numeroContribuinte;
	private long shop;
	@NotNull(message = "Localidade obrigatória.")
	private Long location;
	private long role;
	private String username;
	private MultipartFile file;
	public EmployeeDTO() {
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
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
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
	public long getShop() {
		return shop;
	}
	public void setShop(long shop) {
		this.shop = shop;
	}
	public long getLocation() {
		return location;
	}
	public void setLocation(long location) {
		this.location = location;
	}
	public long getRole() {
		return role;
	}
	public void setRole(long role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	

}
