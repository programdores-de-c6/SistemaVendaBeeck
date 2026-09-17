package com.ideias_inovadora.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ideias_inovadora.repository.EmployeeRepository;

@Service
public class AutenticationService implements UserDetailsService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Override
	
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
	
		 // O 'identifier' pode ser o email ou o username digitado no login
        // Tentamos encontrar o funcionário por qualquer um dos dois caminhos
        return employeeRepository.findByEmailOrUserUsername(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não localizado: " + identifier));
	}

}
