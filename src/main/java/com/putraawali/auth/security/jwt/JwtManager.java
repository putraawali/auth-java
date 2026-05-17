package com.putraawali.auth.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.putraawali.auth.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtManager {
    private final JwtProperties jwtProperties;
    private final Key accessTokenKey;
    private final Key refreshTokenKey;

    public JwtManager(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.accessTokenKey = buildKey(jwtProperties.getAccessToken().getSecretKey());
        this.refreshTokenKey = buildKey(jwtProperties.getRefreshToken().getSecretKey());
    }

    private Key buildKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Instant getExpiry(JwtType jwtType) {
        long minutes = switch (jwtType) {
            case ACCESS -> jwtProperties.getAccessToken().getExpirationMinutes();
            case REFRESH -> jwtProperties.getRefreshToken().getExpirationMinutes();
        };

        return Instant.now().plus(minutes, ChronoUnit.MINUTES);
    }

    private Key signingKey(JwtType jwtType) {
        return switch (jwtType) {

            case ACCESS -> accessTokenKey;

            case REFRESH -> refreshTokenKey;
        };
    }

    private String generateToken(User user, JwtType jwtType) {
        JwtClaims claims = JwtClaims.fromUser(user);
        Date now = new Date();

        Date expiry = Date.from(getExpiry(jwtType));

        return Jwts.builder().
            setSubject(user.getId().toString()).
            addClaims(claims.getClaims()).
            setIssuer(jwtProperties.getIssuer()).
            setIssuedAt(now).
            setExpiration(expiry).
            signWith(signingKey(jwtType)).
            compact();
    }

    public String generateAccessToken(User user) {
        return generateToken(user, JwtType.ACCESS);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, JwtType.REFRESH);
    }

    public boolean validateToken(String token, JwtType jwtType) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey(jwtType))
                .build()
                .parseClaimsJws(token);
                
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token, JwtType jwtType) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey(jwtType))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public JwtClaims getAllClaims(String token, JwtType jwtType) {
        Map<String, Object> claims = Jwts.parserBuilder()
            .setSigningKey(signingKey(jwtType))
            .build()
            .parseClaimsJws(token)
            .getBody();
            
        return JwtClaims.fromClaims(claims);
    }
}
