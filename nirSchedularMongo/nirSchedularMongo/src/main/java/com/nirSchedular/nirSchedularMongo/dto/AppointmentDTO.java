package com.nirSchedular.nirSchedularMongo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class AppointmentDTO {

    private String id;   // Unique identifier for the appointment, which MongoDB will generate automatically
    private List<BookingDTO> bookings;   // List of bookings associated with this appointment

    @Override
    public String toString() {
        return "AppointmentDTO{" +
                "id='" + id + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
