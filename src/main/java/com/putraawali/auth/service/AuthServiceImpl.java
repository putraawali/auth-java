package com.putraawali.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.codec.digest.DigestUtils;

import com.putraawali.auth.dto.request.LoginRequest;
import com.putraawali.auth.dto.request.RegisterRequest;
import com.putraawali.auth.dto.response.TokenResponse;
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
    public TokenResponse login(LoginRequest req) {
        final String email = req.getEmail();
        final String password = req.getPassword();

        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        // Revoke all existing sessions for the user to prevent multiple active sessions (optional, can be removed for allowing multiple sessions)
        sessionRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtManager.generateAccessToken(user);
        String refreshToken = jwtManager.generateRefreshToken(user);

        Session session = new Session();
        session.setUserId(user.getId());
        session.setTokenHash(hash(refreshToken));
        session.setSessionId(UUID.randomUUID());

        sessionRepository.save(session);

        return new TokenResponse(accessToken, refreshToken);
    }

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }

    @Transactional
    @Override
    public TokenResponse refreshToken(String refreshToken) {
        String tokenHash = hash(refreshToken);
        Session session = sessionRepository.findByTokenHash(tokenHash);

        // 1. Validate session
        if (session == null) {
            throw new AuthException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        // Reuse detection revoked session and all sessions with the same session ID to prevent further abuse
        if (session.getIsRevoked()) {
            sessionRepository.revokeAllBySessionId(session.getSessionId());
            throw new AuthException("Refresh token reuse detected", HttpStatus.UNAUTHORIZED);
        }

        if (session.getAbsoluteExpiration().isBefore(LocalDateTime.now())) {
            throw new AuthException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findById(session.getUserId())
                .orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND));

        String newAccessToken = jwtManager.generateAccessToken(user);
        String newRefreshToken = jwtManager.generateRefreshToken(user);

        // Create new session (rotation + sliding expiration)
        Session newSession = new Session();
        newSession.setUserId(user.getId());
        newSession.setTokenHash(hash(newRefreshToken));
        newSession.setSessionId(session.getSessionId()); // Reuse session ID for sliding expiration (session grouping)
        newSession.setAbsoluteExpiration(session.getAbsoluteExpiration()); // Reuse absolute expiration
        sessionRepository.save(newSession);

        // Revoke old session
        session.setIsRevoked(true);
        sessionRepository.save(session);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
    
    @Override
    public void logout(int customerId) {
        sessionRepository.revokeAllByUserId((long) customerId);
    }
}
