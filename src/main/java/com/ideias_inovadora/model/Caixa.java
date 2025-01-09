package com.ideias_inovadora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "caixa")
public class Caixa implements Serializable{


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "loja_fk")
	private Loja loja;
	private Funcionario funcionarioAbri;
	private Funcionario funcionarioFech;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;
	private StatusCaixa statusCaixa;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataFecho;
	public Caixa() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Loja getLoja() {
		return loja;
	}
	public void setLoja(Loja loja) {
		this.loja = loja;
	}
	public Funcionario getFuncionarioAbri() {
		return funcionarioAbri;
	}
	public void setFuncionarioAbri(Funcionario funcionarioAbri) {
		this.funcionarioAbri = funcionarioAbri;
	}
	public Funcionario getFuncionarioFech() {
		return funcionarioFech;
	}
	public void setFuncionarioFech(Funcionario funcionarioFech) {
		this.funcionarioFech = funcionarioFech;
	}
	public BigDecimal getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}
	public BigDecimal getValorFinal() {
		return valorFinal;
	}
	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}
	public StatusCaixa getStatusCaixa() {
		return statusCaixa;
	}
	public void setStatusCaixa(StatusCaixa statusCaixa) {
		this.statusCaixa = statusCaixa;
	}
	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public LocalDateTime getDataFecho() {
		return dataFecho;
	}
	public void setDataFecho(LocalDateTime dataFecho) {
		this.dataFecho = dataFecho;
	}
	
	

}
