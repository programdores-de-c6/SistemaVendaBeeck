package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideias_inovadora.config.STNCurrencySerializer;
import com.ideias_inovadora.model.Category;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.Supplier;
import com.ideias_inovadora.model.Tax;
import jakarta.validation.constraints.Size;

/**
 * DATA TRANSFER OBJECT (DTO) - PRODUTO
 * Este objeto transporta dados entre o React e o Java.
 * Unifica informações da tabela 'Product' e da tabela 'Stock'.
 */
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// --- IDENTIDADE GLOBAL (Tabela Product) ---
	private long id;
	
	@Size(min = 1, max = 50)
	private String codigobarra;
	
	@Size(min = 1, max = 100, message = "Tamanho de caracteres excedido.")
	private String nome;
	
	@Size(max = 150, message = "Tamanho de caracteres excedido.")
	private String descricao;
	private Long  storeId;
	// Flag que define se o sistema deve validar quantidades (Físico vs Serviço)
	private boolean controlaStock;

	// --- DADOS COMERCIAIS (Vindos da tabela Stock conforme a Loja) ---
	
	/**
	 * Preço Unitário de Venda.
	 * ✅ IMPORTANTE: No nosso modelo, este valor é salvo na tabela Stock.
	 * No DTO ele serve para receber o valor do formulário ou mostrar na lista.
	 */
	@JsonSerialize(using = STNCurrencySerializer.class)
	private BigDecimal precoUnitario;

	// Quantidade atual em stock na loja selecionada
	private int stock;
	
	// Limite mínimo para alertas de reposição
	private int stockMin;

	// --- RELACIONAMENTOS (OBJETOS COMPLETOS) ---
	// Usados principalmente no salvamento (@RequestBody)
	private Category category;
	private Tax tax;
	private Supplier supplier;
	private Employee employee;

	// --- RELACIONAMENTOS (CAMPOS PLANOS / FLATTENING) ---
	// Usados para facilitar a leitura no Frontend (Tabelas e Detalhes)
	private long idcatgory;
	private String categorys;
	private long idtax;
	private String taxs;
	private BigDecimal taxa; // Valor percentual do imposto (ex: 15.00)
	private long idsupplier;
	private String suppliers;
	private String employees;

	// --- GESTÃO DE ARQUIVOS E IMAGENS ---
	// Recebe o arquivo binário no upload
	private MultipartFile file;
	// Devolve a URL ou string Base64 para o navegador exibir a imagem
	private String logoUrl;

	// --- GESTÃO MULTI-LOJA ---
	/**
	 * Lista de distribuições iniciais.
	 * Usada pelo Admin Master para definir stock em várias lojas ao mesmo tempo.
	 */
	private List<StockDTO> stockDTOs;
	
	// Define a ação no update de stock (ex: "adicionar" ou "baixar")
	private String acao;

	// --- DATAS DE CONTROLE ---
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime datacriacao;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataAtualizacao;

	// --- CONSTRUTORES ---
	public ProductDTO() {
		super();
	}

	// --- GETTERS E SETTERS ---
	// (Mantenha os métodos conforme a necessidade do seu código Java)

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getCodigobarra() { return codigobarra; }
	public void setCodigobarra(String codigobarra) { this.codigobarra = codigobarra; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }

	public boolean isControlaStock() { return controlaStock; }
	public void setControlaStock(boolean controlaStock) { this.controlaStock = controlaStock; }

	public Category getCategory() { return category; }
	public void setCategory(Category category) { this.category = category; }

	public Tax getTax() { return tax; }
	public void setTax(Tax tax) { this.tax = tax; }

	public Supplier getSupplier() { return supplier; }
	public void setSupplier(Supplier supplier) { this.supplier = supplier; }

	public Employee getEmployee() { return employee; }
	public void setEmployee(Employee employee) { this.employee = employee; }

	public LocalDateTime getDatacriacao() { return datacriacao; }
	public void setDatacriacao(LocalDateTime datacriacao) { this.datacriacao = datacriacao; }

	public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

	public MultipartFile getFile() { return file; }
	public void setFile(MultipartFile file) { this.file = file; }

	public String getLogoUrl() { return logoUrl; }
	public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

	public long getIdcatgory() { return idcatgory; }
	public void setIdcatgory(long idcatgory) { this.idcatgory = idcatgory; }

	public String getCategorys() { return categorys; }
	public void setCategorys(String categorys) { this.categorys = categorys; }

	public long getIdtax() { return idtax; }
	public void setIdtax(long idtax) { this.idtax = idtax; }

	public String getTaxs() { return taxs; }
	public void setTaxs(String taxs) { this.taxs = taxs; }

	public BigDecimal getTaxa() { return taxa; }
	public void setTaxa(BigDecimal taxa) { this.taxa = taxa; }

	public long getIdsupplier() { return idsupplier; }
	public void setIdsupplier(long idsupplier) { this.idsupplier = idsupplier; }

	public String getSuppliers() { return suppliers; }
	public void setSuppliers(String suppliers) { this.suppliers = suppliers; }

	public String getEmployees() { return employees; }
	public void setEmployees(String employees) { this.employees = employees; }

	public List<StockDTO> getStockDTOs() { return stockDTOs; }
	public void setStockDTOs(List<StockDTO> stockDTOs) { this.stockDTOs = stockDTOs; }

	public int getStock() { return stock; }
	public void setStock(int stock) { this.stock = stock; }

	public int getStockMin() { return stockMin; }
	public void setStockMin(int stockMin) { this.stockMin = stockMin; }

	public String getAcao() { return acao; }
	public void setAcao(String acao) { this.acao = acao; }

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	
	
	
}