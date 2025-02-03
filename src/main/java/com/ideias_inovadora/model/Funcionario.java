package com.ideias_inovadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario")
public class Funcionario extends Pessoa {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	@ManyToOne
	@JoinColumn(name = "nivelAcesso_fk")
	private NivelAcesso nivelAcesso;
	@ManyToOne
	@JoinColumn(name = "location_fk")
	private Location location;
	@Enumerated(EnumType.STRING)
	private Status status;
	@ManyToOne
	@JoinColumn(name = "salarioBase_fk")
	private SalarioBase salarioBase;
	public Funcionario() {
		super();
	}

	

	public Shop getShop() {
		return shop;
	}



	public void setShop(Shop shop) {
		this.shop = shop;
	}



	public NivelAcesso getNivelAcesso() {
		return nivelAcesso;
	}

	public void setNivelAcesso(NivelAcesso nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}

	

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SalarioBase getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(SalarioBase salarioBase) {
		this.salarioBase = salarioBase;
	}

}
