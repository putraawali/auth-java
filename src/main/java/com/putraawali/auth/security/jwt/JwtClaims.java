package com.putraawali.auth.security.jwt;

import java.util.Map;

import com.putraawali.auth.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public final class JwtClaims {
    private String email;
    private JwtType type;
    private long customerId;
    
    public Map<String, Object> getClaims() {
        return Map.of(
                "email", email,
                "type", type,
                "customerId", customerId);
    }

    public static JwtClaims fromUser(User user) {
        JwtClaims claims = new JwtClaims(
            user.getEmail(),
            JwtType.ACCESS,
            user.getId()
        );

        return claims;
    }
    
    public static JwtClaims fromClaims(Map<String, Object> claims) {
        JwtClaims jwtClaims = new JwtClaims();
        
        jwtClaims.setEmail((String) claims.get("email"));

        int customerId = (int) claims.get("customerId");
        jwtClaims.setCustomerId(customerId);
        
        String type = (String) claims.get("type");
        jwtClaims.setType(JwtType.valueOf(type));

        return jwtClaims;
    }
}
