package com.ideias_inovadora.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

@Entity
@Table(name = "despesa")
public class Despesa implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	@Enumerated(EnumType.STRING)
	private CategoriaDespesa categoriadespes;
	private String descricao;
	private BigDecimal valor;
	private LocalDate datadespesa;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAutolizacao;
	@ManyToOne
	@JoinColumn(name = "funcionario_fk")
	private Funcionario funcionario;

	public Despesa() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public CategoriaDespesa getCategoriadespes() {
		return categoriadespes;
	}

	public void setCategoriadespes(CategoriaDespesa categoriadespes) {
		this.categoriadespes = categoriadespes;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getDatadespesa() {
		return datadespesa;
	}

	public void setDatadespesa(LocalDate datadespesa) {
		this.datadespesa = datadespesa;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAutolizacao() {
		return dataAutolizacao;
	}

	public void setDataAutolizacao(LocalDateTime dataAutolizacao) {
		this.dataAutolizacao = dataAutolizacao;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

}
