package com.nirSchedular.nirSchedularMongo.service;

import com.nirSchedular.nirSchedularMongo.entity.User;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔁 CustomUserDetailsService called for: " + username);
        User user = userRepository.findByEmail(username)    // Fetch user by email from the database
                .orElseThrow(() -> new UsernameNotFoundException("User Name Not Found"));

        return org.springframework.security.core.userdetails.User   // Create a UserDetails object from the User entity
                .withUsername(user.getEmail())  // Use the email as the username
                .password(user.getPassword())   // Use the stored password (hashed) for authentication
                .roles(user.getRole())  // Assign the user's role (e.g., "USER", "ADMIN") to the UserDetails object
                .build();   // Build and return the UserDetails object
    }

}


/*
   Custom implementation of Spring Security's UserDetailsService.
   Used by Spring Security to fetch user details from the database during authentication.
 */