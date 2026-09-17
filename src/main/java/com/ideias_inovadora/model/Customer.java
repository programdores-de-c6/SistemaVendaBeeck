package com.ideias_inovadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer ")
public class Customer  extends Person {
//Cliente
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "localidade_fk")
	private Location location;
	
	public Customer() {
		super();
	}



	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	
}
