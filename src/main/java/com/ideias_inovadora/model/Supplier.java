package com.ideias_inovadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "supplier")
public class Supplier extends Person {//Fornecedor
	
	private static final long serialVersionUID = 1L;
  
	@ManyToOne
	@JoinColumn(name = "country_fk")
	private Country  country;

	public Supplier() {
		super();
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	

	
	
}
