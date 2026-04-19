 package com.putraawali.auth.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.putraawali.auth.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtManager {
    private final JwtProperties jwtProperties;

    private Key signingKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(User user, long expiration, String type) {
        Map<String, Object> claims = Map.of(
            "email", user.getEmail(),
            "issuer", jwtProperties.getIssuer(),
            "type", type
        );

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder().
            setSubject(user.getId().toString()).
            addClaims(claims).
            setIssuedAt(now).
            setExpiration(expiryDate).
            signWith(signingKey(), SignatureAlgorithm.HS256).
            compact();
    }

    public String generateAccessToken(User user) {
        long tokenExpiresInMs = jwtProperties.getAccessTokenExpiration() * 60 * 1000;
        return generateToken(user, tokenExpiresInMs, "access");
    }

    public String generateRefreshToken(User user) {
        long tokenExpiresInMs = jwtProperties.getRefreshTokenExpiration() * 7 * 60 * 1000;
        return generateToken(user, tokenExpiresInMs, "refresh");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
