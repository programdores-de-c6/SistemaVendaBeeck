package com.ideias_inovadora.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.Status; // Importação do Enum de Status
import com.ideias_inovadora.model.AccessLevel; // Importação do Enum de Acesso
import com.ideias_inovadora.repository.EmployeeRepository;

import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

/**
 * Filtro de interceptação de requisições JWT.
 * Executado uma vez por cada pedido para validar a identidade do utilizador.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private Tokenservice tokenservice;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extração do cabeçalho de autorização
        String authHeader = request.getHeader("Authorization");

        // 2. Verificação se o cabeçalho contém um token Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove a palavra "Bearer "

            try {
                // 3. Recuperação do "Subject" (Email) guardado dentro do Token
                String subject = tokenservice.getSubject(token);

                // 4. Busca do funcionário no banco de dados para validação em tempo real
                Employee employee = employeeRepository.findByEmail(subject)
                        .orElse(null); // Usando Optional conforme reajustamos no Repository

                // 🛡️ VALIDAÇÃO A: Verificação de existência
                if (employee == null) {
                    handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Utilizador não encontrado no sistema.");
                    return;
                }

                // 🛡️ VALIDAÇÃO B: Verificação de conta ativa (Bloqueia demitidos/suspensos na hora)
                if (employee.getStatus() != Status.ACTIVO) {
                    handleError(response, HttpServletResponse.SC_FORBIDDEN, "Esta conta encontra-se inativa ou suspensa.");
                    return;
                }

                // 🛡️ VALIDAÇÃO C: Verificação de nível de acesso (Bloqueia utilizadores sem ROLE)
                if (employee.getAccessLevel() == AccessLevel.NO_ACCESS) {
                    handleError(response, HttpServletResponse.SC_FORBIDDEN, "Não possui permissões para aceder a esta funcionalidade.");
                    return;
                }

                // 5. Criação do objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                employee,
                                null,
                                employee.getAuthorities() // Injeta as permissões (ROLE_ADMIN, etc)
                        );

                // 6. Define a autenticação no contexto global da requisição se ainda estiver vazio
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (TokenExpiredException e) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "A sua sessão expirou. Por favor, faça login novamente.");
                return;
            
            } catch (JWTVerificationException e) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Assinatura digital do token inválida.");
                return;
            
            } catch (Exception e) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Erro na validação da identidade.");
                return;
            }
        }

        // 7. Continua o fluxo normal da requisição
        filterChain.doFilter(request, response);
    }

    private void handleError(HttpServletResponse response, int status, String message) throws IOException {
        // Definimos o status da resposta
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        // Criamos um JSON que imita EXATAMENTE o formato do seu GlobalExceptionHandler
        // Isso garante que o React receba sempre a mesma estrutura
        String json = String.format(
            "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\"}",
            java.time.LocalDateTime.now(),
            status,
            org.springframework.http.HttpStatus.valueOf(status).getReasonPhrase(),
            message
        );

        response.getWriter().write(json);
    }
}