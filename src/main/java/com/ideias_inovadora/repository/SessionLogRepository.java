package com.ideias_inovadora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.SessionLog;

public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {

	List<SessionLog> findByEmployeeAndLogado(Employee employee, boolean b);
	
    SessionLog findByEmployeeAndLogadoTrue(Employee employee);

}
