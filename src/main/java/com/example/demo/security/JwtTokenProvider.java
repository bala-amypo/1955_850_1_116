package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    // ✅ Minimum 256-bit key (required by jjwt)
    private static final String JWT_SECRET =
            "JwtSecretKeyForAutoEvaluationHS256Algorithm123456";

    private static final long JWT_EXPIRATION_MS = 86400000L; // 1 day

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /* =========================================================
       ✅ METHOD EXPECTED BY TESTS (CSV ROLES)
       ========================================================= */
    public String generateToken(Long userId, String email, String rolesCsv) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(email)               // subject = email
                .claim("userId", userId)         // exact claim name
                .claim("email", email)           // exact claim name
                .claim("roles", rolesCsv)        // CSV roles
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* =========================================================
       ✅ OVERLOAD FOR APP USAGE (Set → CSV)
       ========================================================= */
    public String generateToken(Long userId, String email, Set<String> roles) {
        String rolesCsv = String.join(",", roles);
        return generateToken(userId, email, rolesCsv);
    }

    /* =========================================================
       ✅ TEST EXPECTS FALSE FOR INVALID TOKEN
       ========================================================= */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // IMPORTANT: never throw
        }
    }

    /* =========================================================
       ✅ CLAIM EXTRACTION USED BY TESTS
       ========================================================= */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
