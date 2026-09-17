package com.ideias_inovadora.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ideias_inovadora.model.DiscountType;

public class SaleRequestDTO {
    private Long customerId;
    private Long proformaId;
    private Long shopId;
    private Long userId;
    private Long caixaId;
    private BigDecimal discountAmount;
    private String paymentMethod;
    private List<ItemDTO> items;
    private DiscountType discountType; 
    private BigDecimal valorRecebido;
     
    private String customerName;  
    private String customerNif; 
    
    private BigDecimal subtotal;      // Soma dos preços base
    private BigDecimal totalImposto;   // Soma dos IVAs
    private BigDecimal discountValue;  // Valor do desconto calculado em Dobras
    private BigDecimal totalGeral;     // O valor final mostrado ao cliente
    private BigDecimal troco; 
    private BigDecimal totalAntesDoDesconto;
  
    
    // Getters e Setters da classe principal
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCaixaId() { return caixaId; }
    public void setCaixaId(Long caixaId) { this.caixaId = caixaId; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
    
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }

    public Long getProformaId() {
		return proformaId;
	}
	public void setProformaId(Long proformaId) {
		this.proformaId = proformaId;
	}

	// CLASSE INTERNA (Representa os itens do array JSON)
    public static class ItemDTO {
        private Long productId;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal taxRate;

        // Getters e Setters da classe interna
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getTaxRate() { return taxRate; }
        public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    }

	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}
	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotalt(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getTotalImposto() {
		return totalImposto;
	}
	public void setTotalImpostoFront(BigDecimal totalImposto) {
		this.totalImposto = totalImposto;
	}
	public BigDecimal getTotalGeral() {
		return totalGeral;
	}
	public void setTotalGeral(BigDecimal totalGeral) {
		this.totalGeral = totalGeral;
	}
	public BigDecimal getTroco() {
		return troco;
	}
	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public void setTotalImposto(BigDecimal totalImposto) {
		this.totalImposto = totalImposto;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	public BigDecimal getTotalAntesDoDesconto() {
		return totalAntesDoDesconto;
	}
	public void setTotalAntesDoDesconto(BigDecimal totalAntesDoDesconto) {
		this.totalAntesDoDesconto = totalAntesDoDesconto;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerNif() {
		return customerNif;
	}
	public void setCustomerNif(String customerNif) {
		this.customerNif = customerNif;
	}
    
    
}