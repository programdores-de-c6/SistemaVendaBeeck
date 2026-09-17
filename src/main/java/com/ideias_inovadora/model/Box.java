package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "box")
public class Box implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "employeeclosure_fk")
	private Employee employeeClosure;
	@ManyToOne
	@JoinColumn(name = "employeeOpened_fk")
	private Employee employeeOpened;
	private BigDecimal valorInicial;
	private BigDecimal valorDia;
	private BigDecimal valorFinal;
	@Enumerated(EnumType.STRING)
	private StatusCaixa statusCaixa;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataFecho;
	@ManyToOne
	@JoinColumn(name = "shop_fk", nullable = false)
	private Shop shop;
	
	public Box() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public Employee getEmployeeClosure() {
		return employeeClosure;
	}
	public void setEmployeeClosure(Employee employeeClosure) {
		this.employeeClosure = employeeClosure;
	}
	public Employee getEmployeeOpened() {
		return employeeOpened;
	}
	public void setEmployeeOpened(Employee employeeOpened) {
		this.employeeOpened = employeeOpened;
	}
	public BigDecimal getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}
	public BigDecimal getValorFinal() {
		return valorFinal;
	}
	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}
	
	public BigDecimal getValorDia() {
		return valorDia;
	}
	public void setValorDia(BigDecimal valorDia) {
		this.valorDia = valorDia;
	}
	public StatusCaixa getStatusCaixa() {
		return statusCaixa;
	}
	public void setStatusCaixa(StatusCaixa statusCaixa) {
		this.statusCaixa = statusCaixa;
	}
	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public LocalDateTime getDataFecho() {
		return dataFecho;
	}
	public void setDataFecho(LocalDateTime dataFecho) {
		this.dataFecho = dataFecho;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	

}
