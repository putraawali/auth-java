package com.putraawali.auth.service;

import com.putraawali.auth.dto.request.LoginRequest;
import com.putraawali.auth.dto.request.RegisterRequest;
import com.putraawali.auth.dto.response.TokenResponse;

public interface AuthService {
    void register(RegisterRequest req);

    TokenResponse login(LoginRequest req);
    
    // Refresh token implement sliding session based on absolute expiration
    TokenResponse refreshToken(String refreshToken);
}
