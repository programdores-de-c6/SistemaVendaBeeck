package com.ideias_inovadora.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "presenca")
public class Presenca implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Funcionario funcionario;
	private Funcionario funcSistema;
	@Enumerated(EnumType.STRING)
	private StatusPresenca statusPresenca;
	LocalDateTime data;
	public Presenca() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Funcionario getFuncSistema() {
		return funcSistema;
	}
	public void setFuncSistema(Funcionario funcSistema) {
		this.funcSistema = funcSistema;
	}
	public StatusPresenca getStatusPresenca() {
		return statusPresenca;
	}
	public void setStatusPresenca(StatusPresenca statusPresenca) {
		this.statusPresenca = statusPresenca;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}

	
	
}
