package com.morris.opensquare.models.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AuthRequest {
    private String username;
    private String password;
}
