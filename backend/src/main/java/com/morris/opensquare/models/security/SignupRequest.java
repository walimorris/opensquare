package com.morris.opensquare.models.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@Builder
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String emailAddress;
    private String organization;
    private String profession;
    private String ageGroup;
    private MultipartFile image;
}
