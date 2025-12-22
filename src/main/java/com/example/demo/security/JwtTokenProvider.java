package com.example.demo.security;

import java.util.Set;

public class JwtTokenProvider {
    public String generateToken(Long userId, String email, Set<String> roles) { return null; }
    public String getUsername(String token) { return null; }
    public Set<String> getRole(String token) { return null; }
    public Object getClaims(String token) { return null; }
    public boolean validateToken(String token) { return false; }
}