package com.nirSchedular.nirSchedularMongo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service                     // This annotation indicates that this class is a service component in the Spring context
public class JWTUtils {     // This class is responsible for generating and validating JWT tokens

    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; //expires in 7 days

    @Value("${jwt.secret}") // This annotation is used to inject the secret key from application properties
    private String secretString;

    private SecretKey key;

    @PostConstruct          // This annotation indicates that this method should be called after the bean is constructed and all dependencies are injected
    public void init() {   // This method is called to initialize the secret key
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8); // no Base64 decode
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");   // Create a SecretKey from the byte array using HmacSHA256 algorithm
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()                       // This method generates a JWT token for the given user details
                .subject(userDetails.getUsername()) // Set the subject of the token to the username
                .claim("roles", userDetails.getAuthorities().stream()   // Add user roles as a claim in the token
                        .map(auth -> auth.getAuthority())   // Map each authority to its string representation
                        .toList())            // Convert the stream to a list of roles
                .issuedAt(new Date(System.currentTimeMillis())) // Set the issued date of the token to the current time
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set the expiration date of the token to 7 days from now
                .signWith(this.key) // Sign the token with the secret key
                .compact(); // Compact the token into a string representation
    }

    public String extractUserName(String token){    // This method extracts the username from the JWT token
        return extractClaims(token, Claims::getSubject);    // Extract the subject (username) from the token claims
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){    // This method extracts claims from the JWT token using the provided function
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isValidToken(String token, UserDetails userDetails){ // This method checks if the token is valid for the given user details
        final String username = extractUserName((token));
        return (username.equals(userDetails.getUsername()) && !isTokenExpires(token));
    }

    public boolean isTokenExpires(String token){    // This method checks if the token has expired
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}


/*

What matters about the secret key:

It must be kept secret (never push it to GitHub or expose it publicly).

It must be strong (long, random, hard to guess — avoid predictable values like mysecret).

It must be consistent:

If your backend restarts, it must still use the same key to validate previously issued tokens.

If you change the key, all previously issued tokens become invalid.

*/
