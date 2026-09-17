package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BoxRequestDTO implements Serializable  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
    private long employeeId; // ID de quem abre ou fecha
    private BigDecimal valor; // Valor inicial ou final
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

    
    
}
