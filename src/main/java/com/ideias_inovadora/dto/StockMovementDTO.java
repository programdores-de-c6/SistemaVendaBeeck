package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideias_inovadora.model.MovementType;

public class StockMovementDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	  private Long id;
	    private String motivo;
	    private int quantidade;
	    private MovementType movementType;
	    @JsonFormat(pattern = "dd/MM/yyy HH:mm")
	    private LocalDateTime data;
	    private String product;
	    private String shop;
	    private String employeeName;
		public StockMovementDTO() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getMotivo() {
			return motivo;
		}
		public void setMotivo(String motivo) {
			this.motivo = motivo;
		}
		public int getQuantidade() {
			return quantidade;
		}
		public void setQuantidade(int quantidade) {
			this.quantidade = quantidade;
		}
		public MovementType getMovementType() {
			return movementType;
		}
		public void setMovementType(MovementType movementType) {
			this.movementType = movementType;
		}
		public LocalDateTime getData() {
			return data;
		}
		public void setData(LocalDateTime data) {
			this.data = data;
		}
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public String getShop() {
			return shop;
		}
		public void setShop(String shop) {
			this.shop = shop;
		}
		public String getEmployeeName() {
			return employeeName;
		}
		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}
	    
	    
	
}
