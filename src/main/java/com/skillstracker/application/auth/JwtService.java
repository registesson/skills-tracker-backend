package com.skillstracker.application.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:mySecretKeyThatIsVeryLongAndSecureForJwtOperations12345}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            return Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }

    public String generateToken(String email) {
        SecretKey key = getSigningKey();
        // Implementation for generating JWT token
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractEmail(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        // Implementation for extracting email from JWT token
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        // Implementation for validating JWT token
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
