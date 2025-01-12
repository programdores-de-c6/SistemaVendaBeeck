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
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "requisicao")
public class Requisicao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@DecimalMin("0.00")
	private BigDecimal primeiraPrestacao;
	@DecimalMin("0.00")
	private BigDecimal segundaPrestacao;
	@Enumerated(EnumType.STRING)
	private StatusRequisicao statusRequisicao;
	private String descricao;
	private String nomeRequerente;
	private String obsFuncionario;
	private LocalDateTime dataRequisao;
	private int duracao;
	private LocalDate dataReceber;
	@ManyToOne
	@JoinColumn(name = "transacao_fk")
	private Transacao transacao;

	public Requisicao() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public BigDecimal getPrimeiraPrestacao() {
		return primeiraPrestacao;
	}

	public void setPrimeiraPrestacao(BigDecimal primeiraPrestacao) {
		this.primeiraPrestacao = primeiraPrestacao;
	}

	public BigDecimal getSegundaPrestacao() {
		return segundaPrestacao;
	}

	public void setSegundaPrestacao(BigDecimal segundaPrestacao) {
		this.segundaPrestacao = segundaPrestacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeRequerente() {
		return nomeRequerente;
	}

	public void setNomeRequerente(String nomeRequerente) {
		this.nomeRequerente = nomeRequerente;
	}

	public String getObsFuncionario() {
		return obsFuncionario;
	}

	public void setObsFuncionario(String obsFuncionario) {
		this.obsFuncionario = obsFuncionario;
	}

	public LocalDateTime getDataRequisao() {
		return dataRequisao;
	}

	public void setDataRequisao(LocalDateTime dataRequisao) {
		this.dataRequisao = dataRequisao;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public LocalDate getDataReceber() {
		return dataReceber;
	}

	public void setDataReceber(LocalDate dataReceber) {
		this.dataReceber = dataReceber;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public StatusRequisicao getStatusRequisicao() {
		return statusRequisicao;
	}

	public void setStatusRequisicao(StatusRequisicao statusRequisicao) {
		this.statusRequisicao = statusRequisicao;
	}

}
