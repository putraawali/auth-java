package com.putraawali.auth.security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.putraawali.auth.dto.request.UserPrincipal;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtManager jwtManager;

    public JwtAuthFilter(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // No token, continue without authentication
        }

        String token = authHeader.substring(7);
        try {
            boolean isValid = jwtManager.validateToken(token);
            if (isValid) {
                Map<String, Object> claims = jwtManager.getAllClaims(token);
                UserPrincipal userPrincipal = new UserPrincipal();
                userPrincipal.setEmail((String) claims.get("email"));
                userPrincipal.setCustomerId(((Number) claims.get("customerId")).intValue());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, List.of() // No authorities for now
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Invalid token, you can choose to reject the request or just continue without authentication
            // For example, you can set response status to 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            SecurityContextHolder.clearContext(); // Clear any existing security context
            return; // Stop further processing
        }
        
        filterChain.doFilter(request, response);
    }
}
