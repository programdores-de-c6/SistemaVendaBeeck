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

@Entity
@Table(name = "trasacao")
public class Transacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "cliente_fk")
	private Cliente cliente;
	@ManyToOne
	@JoinColumn(name = "funcionario_fk")
	private Funcionario funcionario;
	@Enumerated(EnumType.STRING)
	private MetodoPagamento metodoPagamento;
	@DecimalMin("0.01")
	private BigDecimal totalGeral;
	private LocalDateTime datatrasacao;
	@Enumerated(EnumType.STRING)
	private TipoTrasacao tipoTrasacao;
	@JoinColumn(name = "loja_fk")
	private Loja loja;

	public Transacao() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public MetodoPagamento getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
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

	public TipoTrasacao getTipoTrasacao() {
		return tipoTrasacao;
	}

	public void setTipoTrasacao(TipoTrasacao tipoTrasacao) {
		this.tipoTrasacao = tipoTrasacao;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

}
