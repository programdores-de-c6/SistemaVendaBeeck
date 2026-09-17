package com.ideias_inovadora.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventoryLocation")
public class InventoryLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nome;
	private LocalDateTime datacriacao;
	private LocalDateTime dataAtualizacao;
	public InventoryLocation() {
		super();
	}
	public synchronized long getId() {
		return id;
	}
	public synchronized void setId(long id) {
		this.id = id;
	}
	public synchronized String getNome() {
		return nome;
	}
	public synchronized void setNome(String nome) {
		this.nome = nome;
	}
	public synchronized LocalDateTime getDatacriacao() {
		return datacriacao;
	}
	public synchronized void setDatacriacao(LocalDateTime datacriacao) {
		this.datacriacao = datacriacao;
	}
	public synchronized LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}
	public synchronized void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	} 

}
