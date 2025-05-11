package com.nirSchedular.nirSchedularMongo.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data // Lombok generates getters, setters, toString, equals, and hashCode methods
@Document(collection = "users") // This class maps to the 'users' collection in MongoDB (not 'collation')
public class User implements UserDetails {

    @Id // Marks this field as the primary identifier for the MongoDB document
    private String id;  // Unique identifier for each user, which MongoDB will generate automatically

    @NotBlank(message = "Email number is required") // Ensures Email is not blank
    private String email; // Email of the user
    private String name; // Name of the user
    private String phoneNumber; // Phone number of the user

    private String password; // User's password
    private String role; // User's role (e.g., "ROLE_ADMIN", "ROLE_USER")

    @DBRef // References another document in MongoDB (in this case, Booking documents)
    private List<Booking> bookings = new ArrayList<>(); // List of bookings associated with the user

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role)); // Grants authority based on role
    }

    @Override
    public String getUsername() {
        return email; // Email is used as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // By default, account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // By default, account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // By default, credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // By default, account is enabled
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
