package com.putraawali.auth.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import com.putraawali.auth.dto.request.LoginRequest;
import com.putraawali.auth.dto.request.RegisterRequest;
import com.putraawali.auth.dto.response.LoginResponse;
import com.putraawali.auth.entity.Session;
import com.putraawali.auth.entity.User;
import com.putraawali.auth.exception.AuthException;
import com.putraawali.auth.repository.SessionRepository;
import com.putraawali.auth.repository.UserRepository;
import com.putraawali.auth.security.jwt.JwtManager;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;

    public AuthServiceImpl(
        UserRepository userRepository,
        SessionRepository sessionRepository,
        PasswordEncoder passwordEncoder,
        JwtManager jwtManager
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtManager = jwtManager;
    }

    @Override
    public void register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()) != null) {
            throw new AuthException("Email already registered", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = passwordEncoder.encode(req.getPassword());

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }


    @Override
    public LoginResponse login(LoginRequest req) {
        final String email = req.getEmail();
        final String password = req.getPassword();

        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = jwtManager.generateAccessToken(user);
        String refreshToken = jwtManager.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }
    
    private void saveRefreshToken(User user, String refreshToken) {
        Session session = new Session();
        session.setUserId(user.getId());
        session.setTokenHash(hash(refreshToken));
        session.setSessionId(UUID.randomUUID());

        sessionRepository.save(session);
    }

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
