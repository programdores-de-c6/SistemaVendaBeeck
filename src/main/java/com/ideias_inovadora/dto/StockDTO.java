package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideias_inovadora.config.STNCurrencySerializer;



public class StockDTO implements Serializable{


	
	private static final long serialVersionUID = 1L;


	private int stock;
    private int stockMin;

	private Long  product;
  
	private Long  storeId;
	@JsonSerialize(using = STNCurrencySerializer.class)
	private BigDecimal precoUnitario;
	
	
	
	
	
	public StockDTO() {
		super();
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStockMin() {
		return stockMin;
	}

	public void setStockMin(int stockMin) {
		this.stockMin = stockMin;
	}



	public Long getProduct() {
		return product;
	}

	public void setProduct(Long product) {
		this.product = product;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}


	

	
	

}
