package com.br.artefrequencia.ApiArteFrequencia.security;

import java.util.Date;
import java.time.Duration; 

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Usuario;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Duration accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Duration refreshExpiration;

    public String generateAccessToken(Usuario user) {
     
        return buildToken(user, accessExpiration);
    }

    public String generateRefreshToken(Usuario user) {
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("perfil", user.getPerfil().name())
            .claim("type", "refresh")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration.toMillis()))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    
    private String buildToken(Usuario user, Duration expiration) {
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("perfil", user.getPerfil().name())
            .claim("type", "access")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(getClaims(token).get("type"));
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}