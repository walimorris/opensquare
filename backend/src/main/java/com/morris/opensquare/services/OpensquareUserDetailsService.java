package com.morris.opensquare.services;

import com.morris.opensquare.models.security.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface OpensquareUserDetailsService extends UserDetailsService {

    org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    UserDetails save(UserDetails userDetails);
}
