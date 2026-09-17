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
@Table(name = "expense ")
public class Expense implements Serializable {
//Despesa
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "shop_fk")
	private Shop shop;
	@Enumerated(EnumType.STRING)
	private ExpenseCategory  expenseCategory;
	private String descricao;
	private BigDecimal valor;
	private LocalDate datadespesa;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAutolizacao;
	@ManyToOne
	@JoinColumn(name = "employee_fk")
	private Employee employee;

	public Expense() {
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

	

	public ExpenseCategory getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(ExpenseCategory expenseCategory) {
		this.expenseCategory = expenseCategory;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	

}
