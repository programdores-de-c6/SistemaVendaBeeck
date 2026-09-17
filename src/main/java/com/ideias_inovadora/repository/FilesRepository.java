package com.ideias_inovadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Files;

import jakarta.transaction.Transactional;
@Transactional
public interface FilesRepository extends JpaRepository<Files, Long> {

	
	//@Query("SELECT ft FROM ArquivosFotos ft WHERE ft.utilizador.id = ?1")
	//Files busca(long id);
	//@Modifying
	//@Query("DELETE FROM ArquivosFotos ft WHERE ft.utilizador.id = ?1")
	//void deletefoto(long id);

}
