package com.nirSchedular.nirSchedularMongo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.*;

import java.util.List;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
@Builder    // Enables the builder pattern for creating instances of this class
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class Response {

    private int statusCode;   // HTTP status code
    private String message;   // Message to be returned in the response

    private String token;   // Token for authentication or session management
    private String role;   // Role of the user (e.g., admin, user, etc.)
    private String expirationTime;   // Expiration time of the token
    private String bookingConfirmationCode;   // Unique confirmation code for each booking

    private UserDTO user;   // User details
    private AppointmentDTO appointment;   // Appointment details
    private BookingDTO booking;   // Booking details
    private Object data;   // Generic field to hold any additional response data (e.g., lists, objects, messages)
    private List<UserDTO> userList;   // List of users
    private List<AppointmentDTO> appointmentList;   // List of appointments
    private List<BookingDTO> bookingList;   // List of bookings

    public Response(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}

/* This Response DTO is designed to standardize the API responses your backend sends to the frontend or client applications. */