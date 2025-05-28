package com.nirSchedular.nirSchedularMongo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "appointments") // Indicates this is a MongoDB document stored in the 'appointments' collection
@Data                   // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor      // Generates a no-argument constructor
@AllArgsConstructor     // Generates a constructor with all fields
@Builder                // Enables the builder pattern (handy for creating objects, especially in tests or complex services
public class Appointment {

    @Id // Marks this field as the primary identifier for the MongoDB document
    private String id;

    private String userEmail;
    private LocalDate date;
    private LocalTime time;
}
