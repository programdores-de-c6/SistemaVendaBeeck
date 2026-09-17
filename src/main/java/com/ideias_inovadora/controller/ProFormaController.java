package com.ideias_inovadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.dto.ProFormaDTO;
import com.ideias_inovadora.service.ProFormaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/sales-system/proformas")
@CrossOrigin("*")
public class ProFormaController {
    @Autowired private ProFormaService proFormaService;

    @PostMapping
    public ResponseEntity<ProFormaDTO.Response> criar(@Valid  @RequestBody ProFormaDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proFormaService.criar(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProFormaDTO.Response> obter(@PathVariable Long id) {
        return ResponseEntity.ok(proFormaService.obter(id));
    }

    @GetMapping
    public ResponseEntity<List<ProFormaDTO.Response>> listar(
            @RequestHeader(value = "X-Store-ID", required = false) Long shopId) {
        return ResponseEntity.ok(proFormaService.listar(shopId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ProFormaDTO.Response> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(proFormaService.cancelar(id));
    }
    /**
     * A conversão não cria venda. O mesmo detalhe é devolvido para o front
     * carregar no carrinho e permitir alterações antes do checkout.
     */
    @GetMapping("/{id}/convert")
    public ResponseEntity<ProFormaDTO.Response> converter(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                proFormaService.prepararConversao(id)
        );
    }
}