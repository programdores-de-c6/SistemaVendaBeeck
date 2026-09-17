package com.ideias_inovadora.dto;

import java.io.Serializable;

public class PasswordResetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String codigo;
    private String novaSenha;

    public PasswordResetDTO() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}