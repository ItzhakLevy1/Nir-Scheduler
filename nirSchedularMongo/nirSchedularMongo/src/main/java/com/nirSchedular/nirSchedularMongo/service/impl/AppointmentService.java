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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// @Service Marks this class as a service component in the Spring Framework, meaning it's a service that can be injected into other components.
@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;          // Handles database operations for Appointment entities (add, update, delete, find).

    @Autowired
    private BookingRepository bookingRepository;    // Handles operations for the Booking entity to retrieve booking-related data.

    @Override
    public Response addNewAppointment(Appointment appointment) {

        Response response = new Response();
        try {
            Appointment savedAppointment = appointmentRepository.save(appointment);                 // Saves the newly created Appointment entity to the database, The saved entity is assigned to savedAppointment
            AppointmentDTO appointmentDTO = Utils.mapAppointmentEntityToAppointmentDTO(savedAppointment);  // Converts the Appointment entity (savedAppointment) into a appointmentDTO using the Utils.mapAppointmentEntityToAppointmentDTO method

            response.setStatusCode(200);        // Ads a 200 Status Code to the response object
            response.setMessage("New appointment added successfully");  // Ads a Successful message to the response object
            response.setAppointment(appointmentDTO);          // Ads the converted Appointment entity to the response object

        } catch (Exception e) {                 // Handle exceptions
            response.setStatusCode(500);
            response.setMessage("Error occurred while saving an appointment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAppointments() {

        Response response = new Response();     // Creates a new response object

        try {
            List<Appointment> appointmentList = appointmentRepository.findAll();                                 // Fetches all the appointments from the appointmentRepository and stores them in the appointmentList List
            List<AppointmentDTO> appointmentDTOList = Utils.mapAppointmentListEntityToAppointmentListDTO(appointmentList);     // Maps all the rooms to RoomDTOs and stores them into the roomDTOList List

            response.setStatusCode(200);
            response.setMessage("All appointments retrieved successfully");
            response.setAppointmentList(appointmentDTOList);      // Ads the appointmentDTOList List to the response object

        } catch (Exception e) {                     // Handle exceptions
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting all appointments: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAppointmentById(String appointmentId) {

        Response response = new Response();     // Creates a new response object

        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()-> new OurException("Appointment Not Found"));   // Fetches the appointment by its ID, throws OurException if not found
            AppointmentDTO appointmentDTO = Utils.mapAppointmentEntityToAppointmentDTOPlusBookings(appointment);   // Maps the appointment to a appointmentDTO including its booking information

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
    public Response updateAppointment(String appointmentId) {

        Response response = new Response();     // Creates a new response object

        try {

            Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new OurException("Appointment Not Found"));  // Finds the appointment by appointmentId, throws OurException if not found

            Appointment updatedAppointment = appointmentRepository.save(appointment);                       /* Saves the updated appointment back to the database and stores it in updatedAppointment which is an instance of an Appointment object,
                                                                                   If the appointment already exists (based on its ID), it updates the existing entry.
                                                                                   If it’s a new appointment (with no ID or a new ID), it creates a new record.*/
            AppointmentDTO appointmentDTO = Utils.mapAppointmentEntityToAppointmentDTO(updatedAppointment);        // Maps it to updatedAppointment


            response.setStatusCode(200);
            response.setMessage("Appointment was updated successfully");
            response.setAppointment(appointmentDTO);

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

        Response response = new Response();     // Creates a new response object

        try {
            appointmentRepository.findById(appointmentId).orElseThrow(()-> new OurException("Appointment Not Found"));   // Finds a room by its ID
            appointmentRepository.deleteById(appointmentId );     // Deletes the appointment if it exists
            response.setStatusCode(200);            // Ads a 200 Status Code to the response object
            response.setMessage("The appointment was deleted successfully");      // Ads a Successful message to the response object

        } catch (OurException e) {                  // Handles a custom exceptions such as appointment being deleted is not found, and other specific, known conditions.
            response.setStatusCode(404);            // Indicates "Not Found"
            response.setMessage(e.getMessage());
        } catch (Exception e) {                     // Handles all other unexpected exceptions that could happen due to various reasons (e.g., database errors, connection issues etc.)
            response.setStatusCode(500);            // Indicates a server-side error
            response.setMessage("Error occurred while deleting an appointment: " + e.getMessage());
        }
        return response;
    }
    @Override
    public Response removeBookingFromAppointment(String appointmentId, String bookingId) {  // Method to remove a booking from an appointment to allow for rebooking or cancellation

        Response response = new Response();     // Creates a new response object

        try {
            // Try to find the appointment by ID, throw custom exception if not found
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment Not Found"));

            // Try to find the booking by ID, throw custom exception if not found
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new OurException("Booking Not Found"));

            // Try to remove the booking from the appointment's list of bookings
            boolean isRemoved = appointment.getBookings().remove(booking);

            if (isRemoved) {
                Appointment updatedAppointment = appointmentRepository.save(appointment);   // Save the updated appointment
                AppointmentDTO appointmentDTO = Utils.mapAppointmentEntityToAppointmentDTO(updatedAppointment);   // Map updated entity to DTO

                response.setStatusCode(200);
                response.setMessage("Booking was removed from the appointment successfully");
                response.setAppointment(appointmentDTO);
            } else {
                response.setStatusCode(404);
                response.setMessage("The booking is not associated with the specified appointment");
            }

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while removing booking from appointment: " + e.getMessage());
        }

        return response;
    }

}