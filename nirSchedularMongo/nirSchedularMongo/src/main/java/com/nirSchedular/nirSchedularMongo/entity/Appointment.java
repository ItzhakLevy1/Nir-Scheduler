package com.nirSchedular.nirSchedularMongo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "appointments") // Indicates this is a MongoDB document stored in the 'appointments' collection
@Data                   // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor      // Generates a no-argument constructor
@AllArgsConstructor     // Generates a constructor with all fields
@Builder                // Enables the builder pattern (handy for creating objects, especially in tests or complex services)
public class Appointment {

    @Id // Marks this field as the primary identifier for the MongoDB document
    private String id;

    private String userId;    // ID of the user who booked
    private String userEmail; // Email of the user who booked
    private LocalDate date;   // Date of the appointment
    private String timeSlot;  // Suggested values: "MORNING" or "EVENING"
    private boolean booked;   // True if the appointment is booked, false otherwise
    private String confirmationCode;    // Unique confirmation code for the appointment booking
    private String notes;   // Any user notes

    // Constructor for creating an appointment with just the date and time slot, Used in getAllAvailableAppointments
    public boolean isAvailable() {
        return !this.isBooked();
    }

}
