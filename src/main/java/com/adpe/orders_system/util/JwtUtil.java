package com.adpe.orders_system.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adpe.orders_system.DTO.CustomUser;

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

    public String generateToken(CustomUser user) {

        return Jwts.builder()
            .issuedAt(new Date())
            .subject(user.getName())
            .claim("name", user.name) // nombre del usuario
            .claim("role", user.getRol()) // agregar un rol
            .claim("_id", user.get_id()) // _id del usuario
            .expiration(new Date(EXPIRATION_TIME + System.currentTimeMillis()))
            .signWith(getSigningKey())
            .compact();
    }

    public CustomUser extractPayload(String token) {
        return (CustomUser) Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    public boolean validateToken(String token, String username) {
        try {
            return extractPayload(token).getName().equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser() 
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}