package com.putraawali.auth.service;

import com.putraawali.auth.dto.request.LoginRequest;
import com.putraawali.auth.dto.request.RegisterRequest;
import com.putraawali.auth.dto.response.LoginResponse;

public interface AuthService {
    void register(RegisterRequest req);
    LoginResponse login(LoginRequest req);
}
