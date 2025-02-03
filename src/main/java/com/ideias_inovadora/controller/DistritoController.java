package com.ideias_inovadora.controller;  // Certifique-se de estar no pacote correto

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.repository.DistritoRepository;

@RestController
@RequestMapping("api/sistemavenda/distritos")  // Removida a barra no final
public class DistritoController {  // Nome corrigido

    @Autowired
    private DistritoRepository distritoRepository;

    @GetMapping("/listar")  // Melhor prática: Usar "/" no @GetMapping
    public ResponseEntity<?> listar() {
        return new ResponseEntity<>(distritoRepository.findAll(), HttpStatus.OK);
    }
}
