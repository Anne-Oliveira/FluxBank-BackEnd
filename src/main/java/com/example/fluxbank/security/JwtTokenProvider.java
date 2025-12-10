package com.example.fluxbank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:minha-chave-secreta-super-segura-para-jwt-fluxbank-2025}")
    private String secret;

    @Value("${jwt.expiracao:86400000}") // 24 horas em milissegundos
    private Long expiracao;

    public String gerarToken(String documento) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expiracao);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(documento)
                .issuedAt(agora)
                .expiration(dataExpiracao)
                .signWith(key)
                .compact();
    }

    public String getDocumentoDoToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getExpiracao() {
        return expiracao;
    }
}