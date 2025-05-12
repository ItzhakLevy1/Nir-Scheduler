package com.nirSchedular.nirSchedularMongo.service;

import com.nirSchedular.nirSchedularMongo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service    // This annotation indicates that this class is a service component in the Spring context
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired  // This annotation is used to inject the UserRepository bean into this service
    private UserRepository userRepository;

    @Override   // This method is used by Spring Security to load user details during authentication
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Name Not Found")); // Implement the logic to load user details from the database or any other source
    }
}


/*
   Custom implementation of Spring Security's UserDetailsService.
   Used by Spring Security to fetch user details during authentication.
 */