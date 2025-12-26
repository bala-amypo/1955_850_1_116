package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class JwtTokenProvider {
    private String jwtSecret = "super-secret-key";
    private long jwtExpirationMs = 1000000L;

    public String generateToken(Long userId, String email, Set<String> roles) {
        return Jwts.builder()
            .claim("userId", userId)
            .claim("email", email)
            .claim("roles", String.join(",", roles))
            .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(jwtSecret.getBytes())
            .parseClaimsJws(token)
            .getBody();
    }
}
