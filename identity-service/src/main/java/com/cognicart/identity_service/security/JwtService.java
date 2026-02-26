package com.cognicart.identity_service.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final SecretKey key = (SecretKey) Jwts.SIG.HS256.key().build();

    public String generateToken(String email, String role) {

        Instant now = Instant.now();

        return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(Duration.ofHours(1))))
            .signWith(key)
            .compact();
    }
    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role");
    }
}