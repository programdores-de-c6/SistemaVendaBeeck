package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "proformaInvoiceItem") //IntemProforma
public class ProformaInvoiceItem implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "proforma_fk")
	private ProForma proForma;
	@ManyToOne
	@JoinColumn(name = "product_fk")
	private Product product;
	@Min(1)
	private int quantidade;
	@DecimalMin("0.01")
	private BigDecimal precoUnitario;
	@DecimalMin("0.01")
	private BigDecimal subTotal;
	@DecimalMin("0.00")
	private BigDecimal taxaImposto = BigDecimal.ZERO;

	public ProformaInvoiceItem() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ProForma getProForma() {
		return proForma;
	}

	public void setProForma(ProForma proForma) {
		this.proForma = proForma;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTaxaImposto() {
		return taxaImposto;
	}

	public void setTaxaImposto(BigDecimal taxaImposto) {
		this.taxaImposto = taxaImposto;
	}

}
