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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "trasacao")
public class Transaction  implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "cliente_fk")
	private Customer  customer;
	@ManyToOne
	@JoinColumn(name = "box_fk")
	private Box box;
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	@DecimalMin("0.01")
	private BigDecimal totalGeral;
	@DecimalMin("0.00")
	private BigDecimal valorRecebido;
	@DecimalMin("0.00")
	@PositiveOrZero(message = "O valor não pode ser negativo")
	private BigDecimal troco;
	private LocalDateTime datatrasacao;
	@Enumerated(EnumType.STRING)
	private TransactionType  transactionType;
	@ManyToOne 
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	private BigDecimal discountAmount; // O valor/percentagem informado
	@Enumerated(EnumType.STRING)
	private DiscountType discountType; // PERCENTAGE ou FIXED
	private BigDecimal discountValue;  // O valor real subtraído em Dobras (para relatórios)
	private BigDecimal subTotal;
	private BigDecimal totalImposto;

	public Transaction () {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(BigDecimal totalGeral) {
		this.totalGeral = totalGeral;
	}

	public LocalDateTime getDatatrasacao() {
		return datatrasacao;
	}

	public void setDatatrasacao(LocalDateTime datatrasacao) {
		this.datatrasacao = datatrasacao;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public synchronized TransactionType getTransactionType() {
		return transactionType;
	}

	public synchronized void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public BigDecimal getTroco() {
		return troco;
	}

	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTotalImposto() {
		return totalImposto;
	}

	public void setTotalImposto(BigDecimal totalImposto) {
		this.totalImposto = totalImposto;
	}

	

}
