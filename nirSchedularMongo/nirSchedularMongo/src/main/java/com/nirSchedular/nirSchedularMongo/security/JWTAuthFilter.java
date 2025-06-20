package com.nirSchedular.nirSchedularMongo.security;

import com.nirSchedular.nirSchedularMongo.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWTAuthFilter: Intercepts all incoming HTTP requests to validate JWT tokens
 * and sets the security context for authenticated users.
 */
@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils; // Utility class for JWT token generation and validation

    @Autowired
    private CachingUserDetailsService cachingUserDetailsService; // Caching wrapper for UserDetailsService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // If there is no Authorization header or it is empty, skip authentication
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token after the "Bearer " prefix
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUserName(jwtToken);

        // If user email exists and there is no current authentication
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Log the user email for debugging purposes
            System.out.println("✅ Using CachingUserDetailsService to load user: " + userEmail);

            // Load user details using caching service
            UserDetails userDetails = cachingUserDetailsService.loadUserByUsername(userEmail);

            // Validate the JWT token
            if (jwtUtils.isValidToken(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Attach additional details from the request (e.g., remote address)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authenticated user in the security context
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }

        // Continue processing the request
        filterChain.doFilter(request, response);
    }
}



/*
Summary:
This filter is the first line of defense in the application’s security.
It intercepts every request, extracts the JWT token from the Authorization header, validates it, and sets up the security context if the token is valid.

How it Works:
It looks for the Authorization header in the HTTP request.
If present, it extracts the JWT token and uses JWTUtils to validate the token and extract the user information.
It loads user details from the database using CachingUserDetailsService.
If the token is valid, it sets up the security context for the request.
Finally, it continues processing the request by passing it to the next filter in the chain.
*/
