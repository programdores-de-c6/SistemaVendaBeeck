package com.ideias_inovadora.model;

import java.io.Serializable;
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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "stockMovement ")
public class StockMovement  implements Serializable{ //MovimentacaoStock

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Size(min = 1, max = 150, message = "Tamanho de caracteres excedido.")
	private String motivo;
	@Min(1)
	private int quantidade;
    @Enumerated(EnumType.STRING)
	private MovementType  movementType;
	@ManyToOne
	@JoinColumn(name = "produto_fk")
	private Product product;
	@ManyToOne
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	
	private LocalDateTime data;
	@ManyToOne
	@JoinColumn(name = "employee_fk")
	private Employee employee;
	public StockMovement () {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	
	
	public synchronized MovementType getMovementType() {
		return movementType;
	}
	public synchronized void setMovementType(MovementType movementType) {
		this.movementType = movementType;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	
}
