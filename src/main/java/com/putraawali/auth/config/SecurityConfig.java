package com.putraawali.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.putraawali.auth.exception.CustomAuthenticationEntryPoint;
import com.putraawali.auth.exception.JwtAccessDeniedHandler;
import com.putraawali.auth.security.jwt.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint jwtAuthenticatedEntryPoint;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, JwtAccessDeniedHandler jwtAccessDeniedHandler, CustomAuthenticationEntryPoint jwtAuthenticatedEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticatedEntryPoint = jwtAuthenticatedEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
            csrf(csrf -> csrf.disable()).
            authorizeHttpRequests(auth -> 
                auth.
                    requestMatchers(
                        "/health",
                        "/auth/register",
                        "/auth/login"
                    ).
                    permitAll().
                    anyRequest().
                    authenticated()
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).
            exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticatedEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            );
        return http.build();
    }
}
