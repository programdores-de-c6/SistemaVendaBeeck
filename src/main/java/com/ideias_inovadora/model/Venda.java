package com.ideias_inovadora.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "venda")
public class Venda implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "serie_fk")
	private Serie serie;
	private String numeroFactura;
	private String nomeclienteInformal;
	private LocalDateTime datavenda;

	public Venda() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
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

}
