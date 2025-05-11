package com.nirSchedular.nirSchedularMongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
@Document(collection = "appointments") // Indicates this is a MongoDB document stored in the 'appointments' collection

public class Appointment {

    @Id // Marks this field as the primary identifier for the MongoDB document
    private String id;   // Unique identifier for each appointment, which MongoDB will generate automatically

    @DBRef  // Indicates that this field references another MongoDB document
    private List<Booking> bookings = new ArrayList<>();   // List of bookings associated with this appointment from the bookings collection

    @Override // Generates a string representation of the Appointment object
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
