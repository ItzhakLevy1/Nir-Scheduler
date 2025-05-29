package com.nirSchedular.nirSchedularMongo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO {

    private String id;                      // Unique identifier
    private String userEmail;              // User's email
    private LocalDate date;                // Date of the appointment
    private String timeSlot;               // "MORNING" or "EVENING"
    private boolean isBooked;              // True if booked

    private String appointmentType;        // Optional: Business logic type, e.g., "Check-in"
    private String appointmentDescription; // Optional: Description
    private List<BookingDTO> bookings;     // Associated bookings

    @Override
    public String toString() {
        return "AppointmentDTO{" +
                "id='" + id + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", date=" + date +
                ", timeSlot='" + timeSlot + '\'' +
                ", isBooked=" + isBooked +
                ", appointmentType='" + appointmentType + '\'' +
                ", appointmentDescription='" + appointmentDescription + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
