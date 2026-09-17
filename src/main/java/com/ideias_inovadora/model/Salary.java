package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "salary ")
public class Salary  implements Serializable {//Salario

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "employee_fk")
	private Employee employee;
	private BigDecimal descontoSegurancaSocial;
	private BigDecimal descontoFalta;
	private BigDecimal descoIrs;
	private BigDecimal descontaSeguraLoja;
	private BigDecimal salarioLiquido;
	private BigDecimal bonus;
	private LocalDate data;

	public Salary () {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public BigDecimal getDescontoSegurancaSocial() {
		return descontoSegurancaSocial;
	}

	public void setDescontoSegurancaSocial(BigDecimal descontoSegurancaSocial) {
		this.descontoSegurancaSocial = descontoSegurancaSocial;
	}

	public BigDecimal getDescontoFalta() {
		return descontoFalta;
	}

	public void setDescontoFalta(BigDecimal descontoFalta) {
		this.descontoFalta = descontoFalta;
	}

	public BigDecimal getDescoIrs() {
		return descoIrs;
	}

	public void setDescoIrs(BigDecimal descoIrs) {
		this.descoIrs = descoIrs;
	}

	public BigDecimal getDescontaSeguraLoja() {
		return descontaSeguraLoja;
	}

	public void setDescontaSeguraLoja(BigDecimal descontaSeguraLoja) {
		this.descontaSeguraLoja = descontaSeguraLoja;
	}

	public BigDecimal getSalarioLiquido() {
		return salarioLiquido;
	}

	public void setSalarioLiquido(BigDecimal salarioLiquido) {
		this.salarioLiquido = salarioLiquido;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}
