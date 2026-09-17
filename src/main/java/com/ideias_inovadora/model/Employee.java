package com.ideias_inovadora.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// funcionarios
@Entity
@Table(name = "employee")
public class Employee extends Person implements UserDetails {// Funcionario
	private static final long serialVersionUID = 1L;
	// Para funcionários comuns, indica a loja à qual pertencem
	@ManyToOne
	@JoinColumn(name = "shop_fk", nullable = true) // Pode ser nulo
	private Shop shop;
	@ManyToOne
	@JoinColumn(name = "location_fk")
	private Location location;
	@Enumerated(EnumType.STRING)
	private Status status;
	@ManyToOne
	@JoinColumn(name = "jobtitle_fk")
	private JobTitle jobTitle;

	private LocalDate dataAdmissao;

	private LocalDate dataDemissao;
	@OneToOne
	@JoinColumn(name = "file_fk", nullable = true, updatable = true)
	private Files files;

	@OneToOne(mappedBy = "employee")
	private User user;

	public Employee() {
		super();
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public JobTitle getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(JobTitle jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Files getFiles() {
		return files;
	}

	public void setFiles(Files files) {
		this.files = files;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    // Pegamos o AccessLevel (ex: ROLE_ADMIN)
	    AccessLevel level = getAccessLevel();

	    // Se for NO_ACCESS ou nulo, não damos nenhuma permissão
	    if (level == null || level == AccessLevel.NO_ACCESS) {
	        return List.of(); 
	    }
	    // Retornamos a descrição (que já tem o prefixo ROLE_ como mandam as boas práticas)
	    return List.of(new SimpleGrantedAuthority(level.getDescricao()));
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		
		 return this.user != null ? this.user.getSenha() : null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public LocalDate getDataDemissao() {
		return dataDemissao;
	}

	public void setDataDemissao(LocalDate dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public AccessLevel getAccessLevel() {
	    return jobTitle != null ? jobTitle.getAccessLevel() : null;
	}

}
