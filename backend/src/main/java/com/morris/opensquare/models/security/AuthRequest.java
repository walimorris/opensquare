package com.morris.opensquare.models.security;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private String userName;
    private String password;
}
