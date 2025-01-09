package com.ideias_inovadora.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;

@MappedSuperclass
public abstract class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "O campo de nome não pode ser Null")
	@Column(nullable = false)
	private String nome;
	private String dataNascimento;
	private String contactoPrincipal;
	private String contactoSecudario;
	@NotBlank(message = "O campo de nome não pode ser Null")
	@Column(unique = true)
	private String email;
	@Column(updatable = false)
	private String senha;
    @Enumerated(EnumType.STRING)
	private Sexo sexo;
	@Column(unique = true)
	private String numeroBi;
	@Column(unique = true)
	private String numeroContribuinte;
	@Column(updatable = false)
	private LocalDateTime datacriacao;
	private LocalDateTime dataAtualizacao;

	public Pessoa() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
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

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
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

}
