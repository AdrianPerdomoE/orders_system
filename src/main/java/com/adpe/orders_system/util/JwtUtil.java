package com.adpe.orders_system.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY ;

    
    private  long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {

        return Jwts.builder()
            .issuedAt(new Date())
            .subject(username)
            .claim("role", "USER") // Ejemplo de agregar un rol
            .claim("email", "user@example.com") // Ejemplo de agregar un correo electrónico
            .expiration(new Date(EXPIRATION_TIME + System.currentTimeMillis()))
            .signWith(getSigningKey())
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser() 
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}