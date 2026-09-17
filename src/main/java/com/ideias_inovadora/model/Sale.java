package com.ideias_inovadora.model;
import java.io.Serializable;
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
@Table(name = "sale ")
public class Sale  implements Serializable {//Venda

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String numeroFactura;
	private String nomeclienteInformal;
	@ManyToOne
	@JoinColumn(name="serie_fk")
	private Series series;
	private LocalDateTime datavenda;
	@ManyToOne
	@JoinColumn(name = "transaction_fk")
	private Transaction  transaction;

	public Sale () {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getNomeclienteInformal() {
		return nomeclienteInformal;
	}

	public void setNomeclienteInformal(String nomeclienteInformal) {
		this.nomeclienteInformal = nomeclienteInformal;
	}

	public LocalDateTime getDatavenda() {
		return datavenda;
	}

	public void setDatavenda(LocalDateTime datavenda) {
		this.datavenda = datavenda;
	}


	public synchronized Transaction getTransaction() {
		return transaction;
	}

	public synchronized void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}


}
