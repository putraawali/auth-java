package com.putraawali.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.putraawali.auth.dto.request.UserPrincipal;
import com.putraawali.auth.dto.response.ApiResponse;
import com.putraawali.auth.dto.response.GetUserResponse;
import com.putraawali.auth.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        GetUserResponse response = userService.getUserProfile(userPrincipal.getCustomerId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
