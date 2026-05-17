package com.putraawali.auth.service;

import com.putraawali.auth.dto.response.GetUserResponse;

public interface UserService {
    GetUserResponse getUserProfile(long customerId);
}
