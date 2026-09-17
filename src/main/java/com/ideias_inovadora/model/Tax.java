package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax")
public class Tax  implements Serializable{//Imposto

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Column(unique = true, nullable = false)
	private String  imposto;
     private BigDecimal baseCalculo;
    private  BigDecimal valorCalculado;
	public Tax () {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getImposto() {
		return imposto;
	}
	public void setImposto(String imposto) {
		this.imposto = imposto;
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
