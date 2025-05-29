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

    private String userEmail; // Email of the user for whom the appointment is booked
    private LocalDate date;   // Date of the appointment
    private String timeSlot;  // Suggested values: "MORNING" or "EVENING"
    private boolean isBooked; // True if the appointment is booked, false otherwise
}
