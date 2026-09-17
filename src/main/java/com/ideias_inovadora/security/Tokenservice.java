package com.ideias_inovadora.security;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ideias_inovadora.model.Employee;

/**
 * ====================================================
 * SERVIÇO DE TOKENS JWT
 * ====================================================
 *
 * Responsável pela geração e validação dos:
 * - Access Tokens
 * - Refresh Tokens
 *
 * O Refresh Token fica armazenado no Cookie HttpOnly.
 */
@Service
public class Tokenservice {

    /**
     * Chave secreta utilizada para assinar os tokens.
     *
     * IMPORTANTE:
     * Em produção, esta chave deve ficar numa variável
     * de ambiente ou num mecanismo seguro de configuração.
     */
    private final String SECRET =
            "2b55ab61-8e3c-4837-bdcd-b53cb11a1c2f";

    /**
     * Emissor dos tokens.
     */
    private final String ISSUER = "produtos";

    /**
     * Tempo de validade do Access Token.
     *
     * Mantido conforme a configuração actual do sistema.
     */
    private final int ACCESS_TOKEN_EXPIRATION_MINUTES = 1500;

    /**
     * Tempo de validade do Refresh Token.
     */
    private final int REFRESH_TOKEN_EXPIRATION_DAYS = 30;

    /**
     * ====================================================
     * ALGORITMO DE ASSINATURA
     * ====================================================
     */
    private Algorithm getSignAlgorithm() {
        return Algorithm.HMAC256(SECRET);
    }

    /**
     * ====================================================
     * ACCESS TOKEN
     * ====================================================
     *
     * Gera o Access Token utilizado pelo frontend
     * no cabeçalho:
     *
     * Authorization: Bearer <token>
     */
    public String generateAccessToken(
            Employee employee,
            Long sessionLogId) {

        /**
         * Admin Master pode não ter loja associada.
         */
        Long shopId =
                (employee.getShop() != null)
                        ? employee.getShop().getId()
                        : 0L;

        String shopName =
                (employee.getShop() != null)
                        ? employee.getShop().getNome()
                        : "ADMINISTRAÇÃO CENTRAL";

        return JWT.create()

                // Emissor
                .withIssuer(ISSUER)

                // Identificador principal
                .withSubject(employee.getEmail())

                // ID do funcionário
                .withClaim("id", employee.getId())

                // Nível de acesso
                .withClaim(
                        "NivelAcesso",
                        employee.getAccessLevel()
                                .getDescricao()
                )

                // Nome
                .withClaim(
                        "nome",
                        employee.getNome()
                )

                // Loja
                .withClaim(
                        "loja",
                        shopName
                )

                // ID da loja
                .withClaim(
                        "lojaid",
                        shopId
                )

                // ID da sessão
                .withClaim(
                        "sessionLogId",
                        sessionLogId
                )

                // Expiração
                .withExpiresAt(
                        Date.from(
                                LocalDateTime.now()
                                        .plusMinutes(
                                                ACCESS_TOKEN_EXPIRATION_MINUTES
                                        )
                                        .toInstant(
                                                ZoneOffset.UTC
                                        )
                        )
                )

                // Assinatura
                .sign(getSignAlgorithm());
    }

    /**
     * ====================================================
     * REFRESH TOKEN
     * ====================================================
     *
     * O Refresh Token contém o ID da sessão.
     *
     * Isto permite que o sistema verifique se a sessão
     * ainda está activa antes de emitir um novo
     * Access Token.
     */
    public String gerarRefreshToken(
            Employee employee,
            Long sessionLogId) {

        return JWT.create()

                // Emissor
                .withIssuer(ISSUER)

                // E-mail do funcionário
                .withSubject(employee.getEmail())

                // ID do funcionário
                .withClaim(
                        "id",
                        employee.getId()
                )

                // ID da sessão
                .withClaim(
                        "sessionLogId",
                        sessionLogId
                )

                // Expiração em 30 dias
                .withExpiresAt(
                        Date.from(
                                LocalDateTime.now()
                                        .plusDays(
                                                REFRESH_TOKEN_EXPIRATION_DAYS
                                        )
                                        .toInstant(
                                                ZoneOffset.UTC
                                        )
                        )
                )

                // Assinatura
                .sign(getSignAlgorithm());
    }

    /**
     * ====================================================
     * OBTER SUBJECT
     * ====================================================
     *
     * Obtém o e-mail/identificador presente no subject
     * do token.
     *
     * Também valida:
     * - assinatura
     * - issuer
     * - expiração
     */
    public String getSubject(String token) {

        try {

            DecodedJWT decodedJWT =
                    JWT.require(getSignAlgorithm())
                            .withIssuer(ISSUER)
                            .build()
                            .verify(token);

            return decodedJWT.getSubject();

        } catch (JWTDecodeException e) {

            throw new IllegalArgumentException(
                    "Invalid token.",
                    e
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Token inválido ou expirado.",
                    e
            );
        }
    }

    /**
     * ====================================================
     * OBTER ID DA SESSÃO
     * ====================================================
     *
     * Retira do Refresh Token o sessionLogId associado
     * à sessão de login.
     */
    public Long getSessionLogId(String token) {

        try {

            DecodedJWT decodedJWT =
                    JWT.require(getSignAlgorithm())
                            .withIssuer(ISSUER)
                            .build()
                            .verify(token);

            return decodedJWT
                    .getClaim("sessionLogId")
                    .asLong();

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Refresh token inválido.",
                    e
            );
        }
    }

    /**
     * ====================================================
     * OBTER ID DO FUNCIONÁRIO
     * ====================================================
     *
     * Opcionalmente útil para validações futuras.
     */
    public Long getUserId(String token) {

        try {

            DecodedJWT decodedJWT =
                    JWT.require(getSignAlgorithm())
                            .withIssuer(ISSUER)
                            .build()
                            .verify(token);

            return decodedJWT
                    .getClaim("id")
                    .asLong();

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Token inválido.",
                    e
            );
        }
    }

    /**
     * ====================================================
     * VALIDAR TOKEN
     * ====================================================
     *
     * Verifica:
     * - assinatura
     * - issuer
     * - expiração
     */
    public boolean isTokenValid(String token) {

        if (token == null || token.isBlank()) {
            return false;
        }

        try {

            JWT.require(getSignAlgorithm())
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}