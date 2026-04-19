package com.putraawali.auth.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import com.putraawali.auth.dto.response.ApiResponse;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String, String> errorData = Map.of(
            "message", "You don't have permission to access this resource",
            "details", accessDeniedException.getMessage()
        );
        ApiResponse<Map<String, String>> apiResponse = ApiResponse.error(errorData);

        response.getWriter().write(apiResponse.toJson());
    }
}
