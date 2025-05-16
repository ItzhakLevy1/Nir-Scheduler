package com.nirSchedular.nirSchedularMongo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data   // Lombok will generate getters, setters, toString, equals, and hashCode methods
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class BookingDTO {

    private String id;   // Unique identifier for the booking, which MongoDB will generate automatically
    private String bookingConfirmationCode;   // A unique confirmation code for each booking
    private UserDTO user;   // The user associated with this booking
    private LocalDate date;   // The date of the booking
    private AppointmentDTO appointment;   // The appointment associated with this booking

    @Override
    public String toString() {
        return "BookingDTO{" +
                "id='" + id + '\'' +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", userId='" + user + '\'' +
                ", date=" + date +
                ", appointment=" + appointment +
                '}';
    }
}
