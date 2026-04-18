package com.putraawali.auth.service;

import com.putraawali.auth.entity.User;
import com.putraawali.auth.exception.AuthException;
import com.putraawali.auth.repository.UserRepository;
import com.putraawali.auth.dto.request.RegisterRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new AuthException( "Email already registered", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = passwordEncoder.encode(req.getPassword());

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }
}
