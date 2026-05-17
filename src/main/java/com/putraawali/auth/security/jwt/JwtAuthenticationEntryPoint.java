package com.putraawali.auth.security.jwt;

import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.putraawali.auth.dto.response.ApiResponse;
import com.putraawali.auth.dto.response.ErrorResponse;
import com.putraawali.auth.enums.ErrorCodeEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,         
            AuthenticationException authException
        ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse("Invalid or missing token", ErrorCodeEnum.INVALID_TOKEN);
        
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(errorResponse);

        response.getWriter().write(apiResponse.toJson());
    }
    
}
