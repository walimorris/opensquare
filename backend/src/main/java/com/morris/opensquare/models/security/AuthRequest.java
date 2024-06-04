package com.morris.opensquare.models.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AuthRequest {
    private String userName;
    private String password;
}
