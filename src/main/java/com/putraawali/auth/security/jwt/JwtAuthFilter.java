package com.putraawali.auth.security.jwt;

import java.io.IOException;
import java.util.List;

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
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String token = resolveToken(request);
        
        if (IsNeedAuthentication(token)) {
            JwtClaims claims = jwtManager.getAllClaims(token, JwtType.ACCESS);

            UserPrincipal userPrincipal = new UserPrincipal();
            userPrincipal.setEmail(claims.getEmail());
            userPrincipal.setCustomerId(claims.getCustomerId());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, List.of() // No authorities for now
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }

    private boolean IsNeedAuthentication(String token) {
        return token != null && jwtManager.validateToken(token, JwtType.ACCESS) && SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
