package com.nirSchedular.nirSchedularMongo.security;

import com.nirSchedular.nirSchedularMongo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Configuration class to define a CachingUserDetailsService bean
 * that wraps the CustomUserDetailsService for performance optimization.
 * This class also provides a UserDetailsService bean
 * to be used by Spring Security for user authentication.
 * This caching service improves performance by caching user details
 * to avoid repeated database calls during authentication.
 * This is particularly useful in applications with frequent user authentication requests.
 */

@Configuration
public class UserDetailsServiceConfig {

    // This bean wraps the CustomUserDetailsService with a CachingUserDetailsService
    @Bean
    public CachingUserDetailsService cachingUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        return new CachingUserDetailsService(customUserDetailsService);
    }

    // This bean provides the UserDetailsService interface to Spring Security
    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
        return customUserDetailsService;
    }
}
