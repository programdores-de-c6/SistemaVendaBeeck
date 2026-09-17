package com.ideias_inovadora.controller;  // Certifique-se de estar no pacote correto

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.repository.DistrictRepository;

@RestController
@RequestMapping("api/sales-system/distritos")  // Removida a barra no final
public class DistritoController {  // Nome corrigido

    @Autowired
    private DistrictRepository districtRepository;

    @GetMapping("/listar")  // Melhor prática: Usar "/" no @GetMapping
    public ResponseEntity<?> listar() {
        return new ResponseEntity<>(districtRepository.findAll(), HttpStatus.OK);
    }
}
