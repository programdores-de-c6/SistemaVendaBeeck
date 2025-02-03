package com.ideias_inovadora.erros;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por capturar exceções globalmente e retornar respostas personalizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de operações não permitidas.
     * Exemplo: Tentar excluir uma localidade que tem vínculos com clientes.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 409 (CONFLICT) e mensagem de erro.
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleOperationNotAllowed(OperationNotAllowedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Trata exceções quando um recurso não é encontrado.
     * Exemplo: Buscar um ID inexistente no banco de dados.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 404 (NOT FOUND).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(IllegalArgumentException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Trata exceções quando ocorre um erro interno no servidor.
     *
     * @param ex Exceção inesperada.
     * @return Resposta HTTP com código 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse("Erro interno no servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Trata exceções quando o usuário tenta acessar um recurso sem permissão.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 403 (FORBIDDEN).
     
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse("Acesso negado: você não tem permissão para esta ação.", HttpStatus.FORBIDDEN);
    }

    /**
     * Trata exceções quando um argumento inválido é passado em uma requisição.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 400 (BAD REQUEST).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String erro = ex.getBindingResult().getFieldError().getDefaultMessage();
        return buildErrorResponse("Erro de validação: " + erro, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções quando há erro de autenticação.
     * Exemplo: Usuário insere senha errada.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 401 (UNAUTHORIZED).
     
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse("Credenciais inválidas.", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Trata exceções quando o token de autenticação expira.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 401 (UNAUTHORIZED).
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpired(TokenExpiredException ex) {
        return buildErrorResponse("Seu token de acesso expirou. Faça login novamente.", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Trata exceções quando um campo obrigatório não está presente na requisição.
     *
     * @param ex Exceção lançada.
     * @return Resposta HTTP com código 400 (BAD REQUEST).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        return buildErrorResponse("Erro de validação: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Método utilitário para construir respostas de erro de forma padronizada.
     *
     * @param message Mensagem de erro.
     * @param status Código HTTP correspondente.
     * @return ResponseEntity contendo os detalhes do erro.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return ResponseEntity.status(status).body(response);
    }
}
