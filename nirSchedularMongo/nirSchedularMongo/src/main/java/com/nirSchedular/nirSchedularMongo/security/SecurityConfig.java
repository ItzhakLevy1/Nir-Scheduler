package com.nirSchedular.nirSchedularMongo.security;

import com.nirSchedular.nirSchedularMongo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class defines the security configuration for the application,
 * including endpoint access rules, authentication mechanisms, and session policy.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;  // A service that loads user-specific data during authentication

    @Autowired
    private JWTAuthFilter jwtAuthFilter;  // A custom JWT authentication filter that validates JWT tokens on every request

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)  // CSRF is disabled because JWT is stateless and not vulnerable to CSRF in typical REST APIs
                .cors(Customizer.withDefaults())        // Enables CORS with default settings
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // Allow public access to authentication endpoints (login, register, etc.)
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll() // Optional: allow public GET endpoints
                        .requestMatchers("/appointments/**").authenticated()  // 🔐 Secure all appointment endpoints
                        .anyRequest().authenticated()  // Require authentication for any other endpoint
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session (no HTTP session saved on server)
                .authenticationProvider(authenticationProvider())  // Set up the DAO-based authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before Spring's default auth filter

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(customUserDetailsService);  // Inject your custom user loading logic
        daoAuthProvider.setPasswordEncoder(passwordEncoder());  // Set password encoder
        return daoAuthProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use strong password hashing
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();  // Provide authentication manager from Spring context
    }
}



/*
Summary
Purpose: This class defines the security configuration for the application, including endpoint access rules, authentication, and session management.

Main Components:
SecurityFilterChain: Configures the HTTP security settings, such as CORS, CSRF, which endpoints are public, and session management.
JWT Filtering: Adds the JWTAuthFilter to handle JWT token validation.
AuthenticationProvider: Configures how the app retrieves user details and validates passwords during authentication.
Password Encoding: Uses BCryptPasswordEncoder to hash and compare passwords securely.
Authentication Management: Uses the AuthenticationManager to handle the process of authenticating users.

This configuration ensures that only authenticated users can access specific endpoints, while allowing public access to others, and handles JWT token-based authentication in a stateless manner.
 */