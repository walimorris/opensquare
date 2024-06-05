package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.security.UserDetails;
import com.morris.opensquare.repositories.UserRepository;
import com.morris.opensquare.services.OpensquareUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OpensquareUserDetailsServiceImpl implements OpensquareUserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpensquareUserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public OpensquareUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDetails save(UserDetails userDetails) {
        return userRepository.save(userDetails);
    }
}
