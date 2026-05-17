package com.putraawali.auth.security.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import com.putraawali.auth.dto.response.ApiResponse;
import com.putraawali.auth.dto.response.ErrorResponse;
import com.putraawali.auth.enums.ErrorCodeEnum;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse("You don't have permission to access this resource", ErrorCodeEnum.FORBIDDEN);
        ApiResponse<Object> apiResponse = ApiResponse.error(errorResponse);

        response.getWriter().write(apiResponse.toJson());
    }
}
