package com.ecommerce.users.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final long expirationTime;

    public JwtUtils(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-in-ms}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    public String generateToken(Long userId,String email,String role){
        long now = System.currentTimeMillis();
       return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email",email)
                .claim("role",role)
                .issuedAt(new Date(now))
                .expiration(new Date(expirationTime)).signWith(key)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public Long getUserIdFromToken(String token){
        Claims claims = validateToken(token).getBody();
        return Long.valueOf(claims.getSubject());
    }
}
