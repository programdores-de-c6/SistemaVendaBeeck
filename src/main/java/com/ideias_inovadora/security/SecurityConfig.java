
package com.ideias_inovadora.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration

public class SecurityConfig {
	
	@Autowired
	private JwtFilter filter;
	
	

	
	@Bean
	public AuthenticationManager authenticationManager
	(AuthenticationConfiguration authenticationConfiguration)throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
		
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList(
	            "http://localhost:3000", 
	            "http://localhost:3001", 
	            "http://192.168.100.21:3000", 
	            "http://192.168.100.21:3001",
	            "http://192.168.100.19:3001"
	        ));
	    // 2. Permitir todos os métodos
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
	    // 3. Permitir Headers essenciais e o seu header customizado
	    configuration.setAllowedHeaders(Arrays.asList(
	        "Authorization", 
	        "Content-Type", 
	        "X-Requested-With", 
	        "Accept", 
	        "X-Store-ID",
	        "Origin",
	        "Access-Control-Request-Method",
	        "Access-Control-Request-Headers"
	    ));
	    	    configuration.setAllowCredentials(true);
		configuration.setExposedHeaders(Collections.singletonList("Authorization"));
		configuration.setMaxAge(3600L);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

	    
	    return  source;
	}
	/**
	 * Definição da cadeia de filtros de segurança (SecurityFilterChain).
	 */

	/**
	 * Configuração de autorização de pedidos HTTP.
	 * Define o que é público (sem token) e o que é privado (com token).
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        // 1. Desativa CSRF (não necessário para APIs REST com JWT)
	        .csrf(csrf -> csrf.disable())
	        
	        // 2. Aplica a configuração de CORS (definida anteriormente)
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        
	        // 3. Define que o servidor não guardará estado de sessão (Stateless)
	        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	        // 4. Configuração das permissões de rotas
	        .authorizeHttpRequests(authorize -> authorize
	            // ✅ PERMITIR PRE-FLIGHT: Essencial para o CORS funcionar com o IP e headers customizados
	            .requestMatchers(org.springframework.web.cors.CorsUtils::isPreFlightRequest).permitAll()

	            // ✅ ROTAS PÚBLICAS (SISTEMA): Monitorização e Documentação
	            .requestMatchers("/actuator/health").permitAll() // Verificação de saúde do servidor
	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Documentação da API
	            .requestMatchers("/ws/**").permitAll() // Endpoints de WebSocket se existirem

	            // ✅ ROTAS PÚBLICAS (AUTENTICAÇÃO E RECUPERAÇÃO):
	            .requestMatchers("/api/sales-system/employee/login").permitAll() // Login (Início de tudo)
	            .requestMatchers("/api/sales-system/employee/logout").permitAll() // Logout
	            .requestMatchers("/api/sales-system/employee/refresh-token").permitAll() // Renovação do Token
	            
	            // Recuperação de senha
	            .requestMatchers("/api/sales-system/employee/forgot-password").permitAll()
	            .requestMatchers("/api/sales-system/employee/reset-password").permitAll()
	            .requestMatchers("/api/sales-system/employee/updatpassword").permitAll()

	            // ✅ ROTAS DE INSTALAÇÃO/INÍCIO (SEM BARRAS DUPLAS):
	            // Usadas para criar o primeiro funcionário ou buscar dados básicos da loja no login
	            .requestMatchers("/api/sales-system/employee/create").permitAll()
	            .requestMatchers("/api/sales-system/employee/check-setup").permitAll()
	            .requestMatchers("/api/sales-system/employee/setup-initial").permitAll()
	            .requestMatchers("/api/sales-system/employee/insert").permitAll()
	            .requestMatchers("/api/sales-system/distritos/listar").permitAll()
	            .requestMatchers("/api/sales-system/shop/fetch").permitAll() // Para carregar o logo da loja no login

	            // 🔒 TODO O RESTANTE REQUER TOKEN:
	            // Vendas, Produtos, Stock, Categorias, Relatórios, etc.
	            .anyRequest().authenticated()
	        )

	        // 5. Adiciona o nosso filtro JWT antes do filtro padrão de utilizador/senha
	        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
	 




@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
	

	    }
