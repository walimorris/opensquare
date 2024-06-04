package com.morris.opensquare.controllers;

import com.morris.opensquare.models.security.AuthRequest;
import com.morris.opensquare.models.security.JwtTokenProvider;
import com.morris.opensquare.models.security.Roles;
import com.morris.opensquare.models.security.SignupRequest;
import com.morris.opensquare.services.OpensquareUserDetailsService;
import com.morris.opensquare.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
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
    public ModelAndView login(@ModelAttribute AuthRequest request, Model model) {
        LOGGER.info("login info: {}", request.toString());
        try {
            // Note the password is checked inside authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT
            UserDetails userDetails = opensquareUserDetailsService.loadUserByUsername(request.getUserName());
            String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

            // add user details and token to model and redirect to homepage
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            modelAndView.addObject("userDetails", userDetails);
            modelAndView.addObject("token", token);
            return modelAndView;
        } catch (AuthenticationException e) {
            // express errors
            model.addAttribute("error", "Invalid username or password");
            return new ModelAndView("login");
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
        com.morris.opensquare.models.security.UserDetails userDetails = com.morris.opensquare.models.security.UserDetails.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .userName(signupRequest.getUserName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .emailAddress(signupRequest.getEmailAddress())
                .organization(signupRequest.getOrganization())
                .ageGroup(signupRequest.getAgeGroup())
                .roles(List.of(Roles.ROLE_USER))
                .image(imageBase64EncodedStr)
                .build();

        try {
            com.morris.opensquare.models.security.UserDetails userDetailsResult =
                    opensquareUserDetailsService.save(userDetails);
            // success signup, redirect to login
            return new ModelAndView(new RedirectView("/login"));
        } catch (Exception e) {
            // handle and return error message
            model.addAttribute("error", "signup failed: " + e.getMessage());
            return new ModelAndView("signup");
        }
    }
}
