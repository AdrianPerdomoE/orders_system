package com.adpe.orders_system.util;

import com.adpe.orders_system.DTO.CustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Genera un token JWT para un usuario.
     *
     * @param user El usuario para el cual se genera el token.
     * @return El token JWT generado.
     */
    public String generateToken(CustomUser user) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(user.getName())
                .claim("name", user.getName())
                .claim("rol", user.getRol())
                .claim("_id", user.get_id())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el payload del token JWT y lo convierte en un objeto CustomUser.
     *
     * @param token El token JWT.
     * @return El objeto CustomUser extraído del token.
     */
    public CustomUser extractPayload(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Mapea las reclamaciones al objeto CustomUser
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(claims, CustomUser.class);
        } catch (JwtException e) {
            System.err.println("Error al extraer el payload del token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Valida si un token es válido.
     *
     * @param token El token JWT.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.err.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida si un token es válido y pertenece a un usuario específico.
     *
     * @param token    El token JWT.
     * @param username El nombre del usuario.
     * @return true si el token es válido y pertenece al usuario, false en caso contrario.
     */
    public boolean validateToken(String token, String username) {
        try {
            CustomUser user = extractPayload(token);
            return user != null && user.getName().equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un token ha expirado.
     *
     * @param token El token JWT.
     * @return true si el token ha expirado, false en caso contrario.
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            System.err.println("Error al verificar la expiración del token: " + e.getMessage());
            return true; // Considera el token como expirado si ocurre un error
        }
    }
}