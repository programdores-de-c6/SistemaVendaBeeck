package com.ideias_inovadora.model;

import java.io.Serializable;
import java.time.LocalDate;
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
public abstract class Person  implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "O campo de nome não pode ser Null")
	@Column(nullable = false)
	private String nome;
	private String contactoPrincipal;
	private String contactoSecudario;
	private String email;

    @Enumerated(EnumType.STRING)
	private Gender  gender;
	@Column(unique = true)
	private String numeroBi;
	@Column(unique = true)
	private String numeroContribuinte;
	private LocalDate dataNacimento;
	@Column(updatable = false)
	private LocalDateTime datacriacao;
	private LocalDateTime dataAtualizacao;
	

	public Person() {
		super();
	}

	public Person(long id, @NotBlank(message = "O campo de nome não pode ser Null") String nome,
			String contactoPrincipal, String contactoSecudario, String email, Gender gender, String numeroBi,
			String numeroContribuinte, LocalDate dataNacimento, LocalDateTime datacriacao,
			LocalDateTime dataAtualizacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.contactoPrincipal = contactoPrincipal;
		this.contactoSecudario = contactoSecudario;
		this.email = email;
		this.gender = gender;
		this.numeroBi = numeroBi;
		this.numeroContribuinte = numeroContribuinte;
		this.dataNacimento = dataNacimento;
		this.datacriacao = datacriacao;
		this.dataAtualizacao = dataAtualizacao;
	}

	public LocalDate getDataNacimento() {
		return dataNacimento;
	}

	public void setDataNacimento(LocalDate dataNacimento) {
		this.dataNacimento = dataNacimento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
