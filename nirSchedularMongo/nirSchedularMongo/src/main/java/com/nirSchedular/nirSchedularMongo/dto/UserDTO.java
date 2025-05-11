package com.nirSchedular.nirSchedularMongo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
@JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude null fields from JSON serialization
public class UserDTO {

    private String id;   // Unique identifier for the user, which MongoDB will generate automatically
    private String name;   // Name of the user
    private String email;   // Email address of the user
    private String phoneNumber;   // Phone number of the user
    private String role;   // Role of the user (e.g., admin, user, etc.)
    private List<BookingDTO> bookings = new ArrayList<>();   // List of bookings associated with this user

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", bookings='" + bookings + '\'' +
                '}';
    }
}
