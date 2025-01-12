package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "imposto")
public class Imposto implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@ManyToOne
	@JoinColumn(name = "loja_fk")
    private Loja loja;
	@Enumerated(EnumType.STRING)
	private TipoImposto tipoImposto;
     private BigDecimal baseCalculo;
    private  BigDecimal valorCalculado;
	public Imposto() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Loja getLoja() {
		return loja;
	}
	public void setLoja(Loja loja) {
		this.loja = loja;
	}
	public TipoImposto getTipoImposto() {
		return tipoImposto;
	}
	public void setTipoImposto(TipoImposto tipoImposto) {
		this.tipoImposto = tipoImposto;
	}
	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}
	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}
	public BigDecimal getValorCalculado() {
		return valorCalculado;
	}
	public void setValorCalculado(BigDecimal valorCalculado) {
		this.valorCalculado = valorCalculado;
	}
    
    
	}
