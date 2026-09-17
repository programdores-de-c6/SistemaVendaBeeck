package com.ideias_inovadora.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ideias_inovadora.model.DiscountType;

public final class ProFormaDTO {
    private ProFormaDTO() {}

    public static class Request {
        private Long customerId;
        private String customerName;
        private String customerNif;
        private Long shopId;
        private Long employeeId;
        private BigDecimal discountAmount = BigDecimal.ZERO;
        private DiscountType discountType = DiscountType.PERCENTAGE;
        private List<Item> items;

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getCustomerNif() { return customerNif; }
        public void setCustomerNif(String customerNif) { this.customerNif = customerNif; }
        public Long getShopId() { return shopId; }
        public void setShopId(Long shopId) { this.shopId = shopId; }
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
        public DiscountType getDiscountType() { return discountType; }
        public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
        public List<Item> getItems() { return items; }
        public void setItems(List<Item> items) { this.items = items; }
    }

    public static class Item {
        private Long productId;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal taxRate = BigDecimal.ZERO;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getTaxRate() { return taxRate; }
        public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    }

    public static class Response {
        private Long id;
        private String numeroProforma;
        private LocalDateTime dataProforma;
        private Long customerId;
        private String customerName;
        private String customerNif;
        private Long shopId;
        private String status;
        private BigDecimal subtotal;
        private BigDecimal totalImposto;
        private BigDecimal discountValue;
        private BigDecimal totalGeral;
        private List<ItemResponse> items;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNumeroProforma() { return numeroProforma; }
        public void setNumeroProforma(String value) { this.numeroProforma = value; }
        public LocalDateTime getDataProforma() { return dataProforma; }
        public void setDataProforma(LocalDateTime value) { this.dataProforma = value; }
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long value) { this.customerId = value; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String value) { this.customerName = value; }
        public String getCustomerNif() { return customerNif; }
        public void setCustomerNif(String value) { this.customerNif = value; }
        public Long getShopId() { return shopId; }
        public void setShopId(Long value) { this.shopId = value; }
        public String getStatus() { return status; }
        public void setStatus(String value) { this.status = value; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal value) { this.subtotal = value; }
        public BigDecimal getTotalImposto() { return totalImposto; }
        public void setTotalImposto(BigDecimal value) { this.totalImposto = value; }
        public BigDecimal getDiscountValue() { return discountValue; }
        public void setDiscountValue(BigDecimal value) { this.discountValue = value; }
        public BigDecimal getTotalGeral() { return totalGeral; }
        public void setTotalGeral(BigDecimal value) { this.totalGeral = value; }
        public List<ItemResponse> getItems() { return items; }
        public void setItems(List<ItemResponse> value) { this.items = value; }
    }

    public static class ItemResponse {
        private Long productId;
        private String productName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal taxRate;
        private BigDecimal subtotal;

        public Long getProductId() { return productId; }
        public void setProductId(Long value) { this.productId = value; }
        public String getProductName() { return productName; }
        public void setProductName(String value) { this.productName = value; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int value) { this.quantity = value; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal value) { this.unitPrice = value; }
        public BigDecimal getTaxRate() { return taxRate; }
        public void setTaxRate(BigDecimal value) { this.taxRate = value; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal value) { this.subtotal = value; }
    }
}
