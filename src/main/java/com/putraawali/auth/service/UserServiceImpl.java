package com.putraawali.auth.service;

import org.springframework.stereotype.Service;

import com.putraawali.auth.dto.response.GetUserResponse;
import com.putraawali.auth.entity.User;
import com.putraawali.auth.enums.ErrorCodeEnum;
import com.putraawali.auth.exception.AuthServiceException;
import com.putraawali.auth.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetUserResponse getUserProfile(long customerId) {
        User user = userRepository.findById(customerId).
                orElseThrow(() -> new AuthServiceException("User not found", ErrorCodeEnum.DATA_NOT_FOUND));

        return GetUserResponse.from(user);
    }
    
}
