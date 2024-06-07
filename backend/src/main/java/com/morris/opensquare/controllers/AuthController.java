package com.morris.opensquare.controllers;

import com.morris.opensquare.models.security.*;
import com.morris.opensquare.services.OpensquareUserDetailsService;
import com.morris.opensquare.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/opensquare/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final OpensquareUserDetailsService opensquareUserDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider, OpensquareUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.opensquareUserDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        LOGGER.info("login info: {}", request.toString());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = opensquareUserDetailsService.loadUserByUsername(request.getUserName());
            String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(request.getUserName());

            Map<String, Object> response = new HashMap<>();
            response.put("userDetails", userDetails);
            response.put("accessToken", token);
            response.put("refreshToken", refreshToken);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (AuthenticationException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Invalid username or password"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Refresh token is missing");
        }

        try {
            if (jwtTokenProvider.validateToken(refreshToken)) {
                String username = jwtTokenProvider.getUsername(refreshToken);
                UserDetails userDetails = opensquareUserDetailsService.loadUserByUsername(username);

                if (userDetails == null) {
                    return ResponseEntity.status(403).body("Invalid refresh token");
                }

                String newAccessToken = jwtTokenProvider.createToken(username, userDetails.getRoles());
                String newRefreshToken = jwtTokenProvider.createRefreshToken(username);

                return ResponseEntity.ok(Map.of(
                        "accessToken", newAccessToken,
                        "refreshToken", newRefreshToken,
                        "userDetails", userDetails
                ));
            } else {
                Map<String, Object> errorResponse = Map.of(
                        "error", "Invalid refresh token"
                );
                return ResponseEntity.status(403)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Invalid refresh token"
            );
            return ResponseEntity.status(403)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

    @GetMapping("/validate_token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        boolean isValid = jwtTokenProvider.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired jwt token");
        }
    }

    @PostMapping("/signup")
    public ModelAndView signup(@ModelAttribute SignupRequest signupRequest, Model model) {
        LOGGER.info("User Details: {}", signupRequest.toString());
        String imageBase64EncodedStr = null;
        try {
            imageBase64EncodedStr = FileUtil.base64partEncodedStr(signupRequest.getImage());
        } catch (NullPointerException e) {
            LOGGER.error("Image is null: {}", e.getMessage());
        }
        LOGGER.info(imageBase64EncodedStr);
        UserDetails userDetails = com.morris.opensquare.models.security.UserDetails.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .username(signupRequest.getUserName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .emailAddress(signupRequest.getEmailAddress())
                .organization(signupRequest.getOrganization())
                .ageGroup(signupRequest.getAgeGroup())
                .roles(List.of(Roles.ROLE_USER))
                .image(imageBase64EncodedStr)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        try {
            UserDetails userDetailsResult = opensquareUserDetailsService.save(userDetails);
            // success signup, redirect to login
            return new ModelAndView(new RedirectView("/login"));
        } catch (Exception e) {
            // handle and return error message
            model.addAttribute("error", "signup failed: " + e.getMessage());
            return new ModelAndView("signup");
        }
    }
}
