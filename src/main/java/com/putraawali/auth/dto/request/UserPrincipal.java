package com.putraawali.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrincipal {
    private String email;
    private int customerId;
}
