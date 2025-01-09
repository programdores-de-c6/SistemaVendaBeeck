package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "proForma")
public class ProForma implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "cliente_fk")
	private Cliente cliente;
	@ManyToOne
	@JoinColumn(name = "funcionario_fk")
	private Funcionario funcionario;
	@ManyToOne
	@JoinColumn(name = "loja_fk")
	private Loja loja;
	@DecimalMin("0.01")
	private BigDecimal totalGeral;
	private String numeroproforma;
	private String nomeclienteInformal;
	private LocalDateTime dataproforma;

	public ProForma() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	
	public BigDecimal getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(BigDecimal totalGeral) {
		this.totalGeral = totalGeral;
	}

	public String getNomeclienteInformal() {
		return nomeclienteInformal;
	}

	public void setNomeclienteInformal(String nomeclienteInformal) {
		this.nomeclienteInformal = nomeclienteInformal;
	}

	public String getNumeroproforma() {
		return numeroproforma;
	}

	public void setNumeroproforma(String numeroproforma) {
		this.numeroproforma = numeroproforma;
	}

	public LocalDateTime getDataproforma() {
		return dataproforma;
	}

	public void setDataproforma(LocalDateTime dataproforma) {
		this.dataproforma = dataproforma;
	}

	
}
