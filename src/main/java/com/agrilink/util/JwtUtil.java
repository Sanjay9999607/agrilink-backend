package com.agrilink.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String email, String role) {
        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(
                System.currentTimeMillis() + expiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()),
                SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails ud) {
        return extractEmail(token).equals(ud.getUsername())
            && !getClaims(token).getExpiration()
                .before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}