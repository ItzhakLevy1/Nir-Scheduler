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
@Component  // This annotation registers the filter as a Spring component, allowing it to be automatically detected and used in the security configuration.
public class JWTAuthFilter extends OncePerRequestFilter {   // This filter ensures that the JWT token is processed once per request.

    @Autowired  // Inject the JWTUtils utility class to handle JWT operations
    private JWTUtils jwtUtils;  // Utility class for JWT operations (validation, extraction, etc.)

    @Autowired  // Inject the CachingUserDetailsService to load user details from the database
    private CachingUserDetailsService cachingUserDetailsService;    // Service to load user details from the database with caching

    // Skip this filter for login and registration endpoints
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();     // Get the request path to determine if it should be filtered
        return path.startsWith("/auth");    // Skips /auth/login, /auth/register, etc.
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  // This method is called for every request that passes through this filter
            throws ServletException, IOException {

        System.out.println("🔎 JWT Filter triggered for: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");   // Extract the Authorization header from the request

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {  // Check if the header is present and starts with "Bearer "
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // Set response status to 401 Unauthorized
            response.getWriter().write("Missing or invalid Authorization header");  // Write error message to response
            return;
        }

        final String jwtToken = authHeader.substring(7);    // Extract the JWT token by removing the "Bearer " prefix
        final String userEmail = jwtUtils.extractUserName(jwtToken);    // Extract the username (email) from the JWT token

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {  // Check if the user is authenticated
            try {
                UserDetails userDetails = cachingUserDetailsService.loadUserByUsername(userEmail);  // Load user details from the database using the email extracted from the token

                if (jwtUtils.isValidToken(jwtToken, userDetails)) { // Validate the JWT token against the user details
                    SecurityContext context = SecurityContextHolder.createEmptyContext();   // Create a new SecurityContext to hold the authentication information
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(    // Create an authentication token with user details and authorities
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // Set additional details for the authentication token
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                } else {    // If the token is invalid or expired
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // Set response status to 401 Unauthorized
                    response.getWriter().write("Invalid or expired token");
                    return;
                }
            } catch (Exception e) { // Handle any exceptions that occur during user details loading or token validation
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // Set response status to 401 Unauthorized
                response.getWriter().write("User authentication failed");
                return;
            }
        }

        filterChain.doFilter(request, response);    // Continue the filter chain to the next filter or endpoint
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
