package com.ideias_inovadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ideias_inovadora.model.Validation;

import jakarta.transaction.Transactional;

public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Validation findByEmail(String email);

    @Query("""
        SELECT v
        FROM Validation v
        WHERE v.email = ?1
          AND v.codigo = ?2
          AND v.usado = false
    """)
    Validation findByEmailAndCodigo(String email, String codigo);

    @Transactional
    @Modifying
    @Query("UPDATE Validation v SET v.usado = true WHERE v.id = ?1")
    void marcarComoUsado(Long id);
}