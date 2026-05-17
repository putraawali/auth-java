package com.putraawali.auth.dto.response;

import java.time.LocalDateTime;

import com.putraawali.auth.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
public class GetUserResponse {
    private final long id;
    private final String email;
    private final LocalDateTime registeredAt;

    public static GetUserResponse from(User user) {
        return new GetUserResponse(
            user.getId(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}
