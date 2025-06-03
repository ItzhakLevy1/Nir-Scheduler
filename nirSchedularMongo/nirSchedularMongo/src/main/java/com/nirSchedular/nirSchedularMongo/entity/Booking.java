package com.nirSchedular.nirSchedularMongo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a booking for an appointment by a user.
 */
@Document(collection = "bookings") // This marks the class as a MongoDB document stored in the "bookings" collection
@Data // Lombok: generates getters, setters, equals, hashCode, toString
@NoArgsConstructor // Lombok: generates a no-args constructor
@AllArgsConstructor // Lombok: generates a constructor with all fields
@Builder // Lombok: enables the builder pattern for creating instances
public class Booking {

    @Id
    private String id; // MongoDB document ID, maps to _id field in the database

    private Appointment appointment; // The appointment that was booked
    private User user; // The user who made the booking
    private String bookingConfirmationCode; // A unique code used to confirm and identify the booking
    private LocalDate date; // The date of the booking (usually matches appointment date)
}
