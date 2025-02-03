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
	private AccessLevel accessLevel;
	@ManyToOne
	@JoinColumn(name = "location_fk")
	private Location location;
	@Enumerated(EnumType.STRING)
	private Status status;
	@ManyToOne
	@JoinColumn(name = "salarioBase_fk")
	private BaseSalary baseSalary;
	public Funcionario() {
		super();
	}

	

	public Shop getShop() {
		return shop;
	}



	public void setShop(Shop shop) {
		this.shop = shop;
	}



	

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}



	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
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



	public BaseSalary getBaseSalary() {
		return baseSalary;
	}



	public void setBaseSalary(BaseSalary baseSalary) {
		this.baseSalary = baseSalary;
	}


}
