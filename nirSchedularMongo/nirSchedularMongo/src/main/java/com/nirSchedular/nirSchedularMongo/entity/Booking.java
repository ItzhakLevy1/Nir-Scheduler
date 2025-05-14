package com.nirSchedular.nirSchedularMongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
@Document(collection = "bookings") // This class maps to the 'bookings' collection in MongoDB
public class Booking {

    @Id // Marks this field as the primary identifier for the MongoDB document
    private String id;   // Unique identifier for the booking, which MongoDB will generate automatically

    private String bookingConfirmationCode;   // A unique confirmation code for each booking

    @DBRef
    private User user;   // The user associated with this booking

    @DBRef
    private Appointment appointment;   // The appointment associated with this booking

    private LocalDate date;   // The date of the booking

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", user=" + user +
                ", appointment=" + appointment +
                '}';
    }
}
