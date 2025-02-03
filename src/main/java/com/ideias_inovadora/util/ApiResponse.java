package com.ideias_inovadora.util;

import org.springframework.stereotype.Component;

@Component
public class ApiResponse {

    private String status;
    private String message;
    private Object data;

    // Construtor padrão
    public ApiResponse() {
        // Inicialização, se necessário
    }

    // Construtor com parâmetros
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Construtor com parâmetros
    public ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters e Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
