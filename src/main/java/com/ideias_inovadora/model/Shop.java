package com.ideias_inovadora.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Shop")
public class Shop implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "Verifica o nome da localidade ")
	@Column(unique = true, nullable = false)
	private String nome;
	@NotBlank(message = "Verifica o numero de contribuinte ")
	@Column(unique = true, nullable = false)
	private String numeroContribuite;
	private String email;
	private String contacto;
	private int caixaPostal;
	@ManyToOne
	@JoinColumn(name = "created_by_fk")
	private Employee employee;
	@Column(updatable = false)
	private LocalDateTime datacriacao;
	private LocalDateTime dataAtualizacao;
	@ManyToOne
	@JoinColumn(name = "localidade_fk")
	private Location location;
	@ManyToOne	
	@JoinColumn(name = "file_fk")
	private Files files;
    @Enumerated(EnumType.STRING)
    private ShopType shopType;
	

	public Shop() {
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

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public synchronized Employee getEmployee() {
		return employee;
	}

	public synchronized void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public synchronized Files getFiles() {
		return files;
	}

	public synchronized void setFiles(Files files) {
		this.files = files;
	}

	public ShopType getShopType() {
		return shopType;
	}

	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}

	
}
