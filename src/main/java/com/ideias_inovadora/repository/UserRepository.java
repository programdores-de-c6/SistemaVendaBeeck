package com.ideias_inovadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ideias_inovadora.model.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
	
		   boolean existsByUsernameIgnoreCase(String username);

	 @Transactional
	    @Modifying
	    @Query("UPDATE User u SET u.senha = ?1 WHERE u.employee.id = ?2")
	    void updateSenhaByEmployeeId(String novaSenhaEncodada, Long employeeId);
	 
	 @Transactional
	 @Modifying
	 @Query("""
	     UPDATE User u
	     SET u.senha = ?1,
	         u.firstLogin = false
	     WHERE u.employee.id = ?2
	 """)
	 void updateSenhaAndFirstLoginByEmployeeId(
	         String novaSenhaEncodada,
	         Long employeeId
	 );

	 
	 

}
