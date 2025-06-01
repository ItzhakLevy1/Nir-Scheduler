package com.nirSchedular.nirSchedularMongo.service.impl;

import com.nirSchedular.nirSchedularMongo.dto.AppointmentDTO;
import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;
import com.nirSchedular.nirSchedularMongo.entity.Booking;
import com.nirSchedular.nirSchedularMongo.exception.OurException;
import com.nirSchedular.nirSchedularMongo.repo.AppointmentRepository;
import com.nirSchedular.nirSchedularMongo.repo.BookingRepository;
import com.nirSchedular.nirSchedularMongo.service.interfac.IAppointmentService;
import com.nirSchedular.nirSchedularMongo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// @Service Marks this class as a service component in the Spring Framework, meaning it's a service that can be injected into other components.
@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository; // Handles database operations for Appointment entities (add, update, delete, find)

    @Autowired
    private BookingRepository bookingRepository; // Handles operations for the Booking entity to retrieve booking-related data

    @Override
    public Response addNewAppointment(Appointment newAppointment) {
        boolean exists = appointmentRepository.existsByDateAndTimeSlotAndBookedTrue(
                newAppointment.getDate(),
                newAppointment.getTimeSlot()
        );

        if (exists) {
            return Response.builder()
                    .message("An appointment is already booked for this date and time slot.")
                    .statusCode(HttpStatus.CONFLICT.value())
                    .build();
        }

        // Save new appointment
        newAppointment.setBooked(true); // assuming you want to mark it as booked
        appointmentRepository.save(newAppointment);

        return Response.builder()
                .message("Appointment added successfully.")
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }


    @Override
    public Response getAllAppointments() {
        Response response = new Response();
        try {
            List<Appointment> appointmentList = appointmentRepository.findAll(); // Fetch all appointments
            List<AppointmentDTO> appointmentDTOList = Utils.mapAppointmentListEntityToAppointmentListDTO(appointmentList); // Convert to DTO list

            response.setStatusCode(200);
            response.setMessage("All appointments retrieved successfully");
            response.setAppointmentList(appointmentDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting all appointments: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAppointmentById(String appointmentId) {
        Response response = new Response();
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment Not Found")); // Find appointment
            AppointmentDTO appointmentDTO = Utils.mapAppointmentEntityToAppointmentDTOPlusBookings(appointment); // Map with bookings

            response.setStatusCode(200);
            response.setMessage("Appointment retrieved successfully");
            response.setAppointment(appointmentDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting an appointment by id: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateAppointment(String appointmentId, Appointment updatedAppointment) {
        Response response = new Response();
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment Not Found"));

            // Update all fields you allow to change
            appointment.setDate(updatedAppointment.getDate());
            appointment.setTimeSlot(updatedAppointment.getTimeSlot());
            appointment.setBooked(updatedAppointment.isBooked());

            // Save the updated appointment
            Appointment updated = appointmentRepository.save(appointment);
            AppointmentDTO dto = Utils.mapAppointmentEntityToAppointmentDTO(updated);

            response.setStatusCode(200);
            response.setMessage("Appointment was updated successfully");
            response.setAppointment(dto);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating an appointment: " + e.getMessage());
        }
        return response;
    }



    @Override
    public Response deleteAppointment(String appointmentId) {
        Response response = new Response();
        try {
            appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment Not Found")); // Check if exists
            appointmentRepository.deleteById(appointmentId); // Delete
            response.setStatusCode(200);
            response.setMessage("The appointment was deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting an appointment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response removeBookingFromAppointment(String appointmentId, String bookingId) {
        Response response = new Response();
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment not found"));

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new OurException("Booking not found"));

            // Unbook the appointment
            appointment.isBooked(); // Make appointment available again
            appointmentRepository.save(appointment); // Save changes

            // Optionally: delete booking or set status to cancelled (up to your business logic)
            bookingRepository.deleteById(bookingId); // Delete the booking

            response.setStatusCode(200);
            response.setMessage("Booking removed from appointment successfully");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while removing booking from appointment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableAppointmentsByDate(LocalDate date) {
        Response response = new Response();

        try {
            // Fetch appointments for the given date that are not booked
            List<Appointment> availableAppointments = appointmentRepository.findByDateAndBookedFalse(date);

            response.setStatusCode(200);
            response.setMessage("Available appointments retrieved successfully");
            response.setData(availableAppointments);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving available appointments: " + e.getMessage());
        }

        return response;
    }
}
