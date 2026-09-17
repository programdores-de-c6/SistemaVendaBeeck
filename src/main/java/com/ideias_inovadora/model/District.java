package com.ideias_inovadora.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "district")
public class District implements Serializable {//Distrito
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "Erro no campo nome")
	@Column(unique = true, nullable = false)
	@Size(min = 1, max = 100, message = "Tamanho de caracteres excedido.")
	private String nome;
	@Size(min = 1, max = 20, message = "Tamanho de caracteres excedido.")
	private String sigla;
	@ManyToOne
	@JoinColumn(name = "country_fk")
	private Country  country;
	@Column(updatable = false)
	private LocalDateTime datacriacao;
	private LocalDateTime dataAtualizacao;

	public District() {
		super();
	}

	public District(
			@NotBlank(message = "Erro no campo nome") @Size(min = 1, max = 100, message = "Tamanho de caracteres excedido.") String nome,
			@Size(min = 1, max = 20, message = "Tamanho de caracteres excedido.") String sigla, Country country,
			LocalDateTime datacriacao) {
		super();
		this.nome = nome;
		this.sigla = sigla;
		this.country = country;
		this.datacriacao = datacriacao;
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	public Country getCountry() {
		return country;
	}





	public void setCountry(Country country) {
		this.country = country;
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
