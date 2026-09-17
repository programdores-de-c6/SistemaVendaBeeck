package com.ideias_inovadora.dto;

import java.math.BigDecimal;
import java.util.List;

public class SaleResponseDTO {
    // Dados da Venda
    private Long id;
    private String numeroFactura;
    private String numeroAutorizacao;
    private String dataVenda;
    private String nomeCliente;
    private String nifCliente;

    // Dados da Loja (Para o Cabeçalho do Recibo)
    private String shopNome;
    private String shopNif;
    private String shopEndereco;
    private String shopContacto;
    private String shopEmail;
    private String shopLogo; // Base64 para imprimir no recibo

    // Financeiro
    private BigDecimal subtotal;
    private BigDecimal totalImposto;
    private BigDecimal desconto;
    private BigDecimal troco;
    private BigDecimal ValorRecebido;
    private BigDecimal totalGeral;
    private String metodoPagamento;
    private String operador;
    private BigDecimal totalAntesDoDesconto;

    // Itens da Venda
    private List<ItemResponseDTO> itens;
    
  
    public SaleResponseDTO() {
		super();
	}
    

	public String getNifCliente() {
		return nifCliente;
	}


	public void setNifCliente(String nifCliente) {
		this.nifCliente = nifCliente;
	}


	public Long getId() {
		return id;
	}


	public String getNumeroAutorizacao() {
		return numeroAutorizacao;
	}


	public void setNumeroAutorizacao(String numeroAutorizacao) {
		this.numeroAutorizacao = numeroAutorizacao;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(String dataVenda) {
		this.dataVenda = dataVenda;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getShopNome() {
		return shopNome;
	}

	public void setShopNome(String shopNome) {
		this.shopNome = shopNome;
	}

	public String getShopNif() {
		return shopNif;
	}

	public void setShopNif(String shopNif) {
		this.shopNif = shopNif;
	}

	public String getShopEndereco() {
		return shopEndereco;
	}

	public void setShopEndereco(String shopEndereco) {
		this.shopEndereco = shopEndereco;
	}

	public String getShopContacto() {
		return shopContacto;
	}

	public void setShopContacto(String shopContacto) {
		this.shopContacto = shopContacto;
	}

	public String getShopEmail() {
		return shopEmail;
	}

	public void setShopEmail(String shopEmail) {
		this.shopEmail = shopEmail;
	}

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotalImposto() {
		return totalImposto;
	}

	public void setTotalImposto(BigDecimal totalImposto) {
		this.totalImposto = totalImposto;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(BigDecimal totalGeral) {
		this.totalGeral = totalGeral;
	}

	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public List<ItemResponseDTO> getItens() {
		return itens;
	}

	public void setItens(List<ItemResponseDTO> itens) {
		this.itens = itens;
	}

	  public BigDecimal getTroco() {
		return troco;
	}


	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}


	public BigDecimal getTotalAntesDoDesconto() {
		return totalAntesDoDesconto;
	}


	public void setTotalAntesDoDesconto(BigDecimal totalAntesDoDesconto) {
		this.totalAntesDoDesconto = totalAntesDoDesconto;
	}



	public BigDecimal getValorRecebido() {
		return ValorRecebido;
	}


	public void setValorRecebido(BigDecimal valorRecebido) {
		ValorRecebido = valorRecebido;
	}



	public static class ItemResponseDTO {
	        private String productName;
	        private int quantity;
	        private BigDecimal unitPrice;
	        private BigDecimal subtotal;
	        
	        
	        
	        public ItemResponseDTO(String productName, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
	            this.productName = productName;
	            this.quantity = quantity;
	            this.unitPrice = unitPrice;
	            this.subtotal = subtotal;
	        }
	        
	        
	        
			public ItemResponseDTO() {
				super();
			}
			public String getProductName() {
				return productName;
			}
			public void setProductName(String productName) {
				this.productName = productName;
			}
			public int getQuantity() {
				return quantity;
			}
			public void setQuantity(int quantity) {
				this.quantity = quantity;
			}
			public BigDecimal getUnitPrice() {
				return unitPrice;
			}
			public void setUnitPrice(BigDecimal unitPrice) {
				this.unitPrice = unitPrice;
			}
			public BigDecimal getSubtotal() {
				return subtotal;
			}
			public void setSubtotal(BigDecimal subtotal) {
				this.subtotal = subtotal;
			}
	      
	        
	        
	        
	  }

	
	  
}
	        
    
    
    
