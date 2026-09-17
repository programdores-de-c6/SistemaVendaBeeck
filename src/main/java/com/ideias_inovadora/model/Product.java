package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideias_inovadora.config.STNCurrencySerializer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product implements Serializable {// Produto

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Size(min = 1, max = 50)
	 @Column(unique = true, nullable = false)
	private String codigobarra;
	@Size(min = 1, max = 100, message = "Tamanho de caracteres excedido.")
	private String nome;
	@Size( max = 150, message = "Tamanho de caracteres excedido.")
	private String descricao;

	@Column(name = "controla_stock", nullable = false)
	private boolean controlaStock = true; 

	@ManyToOne
	@JoinColumn(name = "category_fk")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "tax_fk")
	private Tax tax;
	@ManyToOne
	@JoinColumn(name = "supplier_fk")
	private Supplier supplier;
	@ManyToOne
	@JoinColumn(name = "employee_fk")
	private Employee employee;
	@Column(updatable = false)
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;
	@ManyToOne	
	@JoinColumn(name = "file_fk")
	private Files files;
	public Product() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigobarra() {
		return codigobarra;
	}

	public void setCodigobarra(String codigobarra) {
		this.codigobarra = codigobarra;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public Files getFiles() {
		return files;
	}

	public void setFiles(Files files) {
		this.files = files;
	}

	public boolean isControlaStock() {
		return controlaStock;
	}

	public void setControlaStock(boolean controlaStock) {
		this.controlaStock = controlaStock;
	}

}
