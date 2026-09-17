package com.ideias_inovadora.dto;

import com.ideias_inovadora.model.ShopType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class InitialSetupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // Dono
    private String adminNome;
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O formato do e-mail é inválido.")
    private String adminEmail;
    private String adminUsername;
    private String adminSenha;
    
    // Loja
    private String shopNome;
    private String shopNif;
    private ShopType shopType;

    // ✅ Novos campos para criar a Localidade na hora
    private String nomeLocalidade; // O que ele digitar
    private Long idDistrito;       // O que ele selecionar no combo
	public InitialSetupDTO() {
		super();
	}
	public String getAdminNome() {
		return adminNome;
	}
	public void setAdminNome(String adminNome) {
		this.adminNome = adminNome;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getAdminUsername() {
		return adminUsername;
	}
	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}
	public String getAdminSenha() {
		return adminSenha;
	}
	public void setAdminSenha(String adminSenha) {
		this.adminSenha = adminSenha;
	}
	public String getShopNome() {
		return shopNome;
	}
	public void setShopNome(String shopNome) {
		this.shopNome = shopNome;
	}
	public String getShopNif() {
		return shopNif;
	}
	public void setShopNif(String shopNif) {
		this.shopNif = shopNif;
	}
	public ShopType getShopType() {
		return shopType;
	}
	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}
	public String getNomeLocalidade() {
		return nomeLocalidade;
	}
	public void setNomeLocalidade(String nomeLocalidade) {
		this.nomeLocalidade = nomeLocalidade;
	}
	public Long getIdDistrito() {
		return idDistrito;
	}
	public void setIdDistrito(Long idDistrito) {
		this.idDistrito = idDistrito;
	}
    

}