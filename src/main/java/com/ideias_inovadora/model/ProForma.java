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
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "proForma")
public class ProForma implements Serializable {//ProForma

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "customer_fk")
	private Customer  customer;
	@ManyToOne
	@JoinColumn(name = "employee_fk")
	private Employee employee;
	@ManyToOne
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	@DecimalMin("0.01")
	private BigDecimal totalGeral;
	private String numeroproforma;
	private String nomeclienteInformal;
	private LocalDateTime dataproforma;
	private String estado = "ABERTA";

	@OneToMany(mappedBy = "proForma", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProformaInvoiceItem> itens = new ArrayList<>();

	public ProForma() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
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

		public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<ProformaInvoiceItem> getItens() {
		return itens;
	}

	public void setItens(List<ProformaInvoiceItem> itens) {
		this.itens = itens;
	}

	public LocalDateTime getDataproforma() {

		return dataproforma;
	}

	public void setDataproforma(LocalDateTime dataproforma) {
		this.dataproforma = dataproforma;
	}

	
}
