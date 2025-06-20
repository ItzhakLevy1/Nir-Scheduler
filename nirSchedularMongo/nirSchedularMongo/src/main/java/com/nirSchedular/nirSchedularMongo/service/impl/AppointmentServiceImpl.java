package com.nirSchedular.nirSchedularMongo.service.impl;

import com.nirSchedular.nirSchedularMongo.dto.AppointmentDTO;
import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;
import com.nirSchedular.nirSchedularMongo.entity.Booking;
import com.nirSchedular.nirSchedularMongo.entity.User;
import com.nirSchedular.nirSchedularMongo.exception.OurException;
import com.nirSchedular.nirSchedularMongo.repo.AppointmentRepository;
import com.nirSchedular.nirSchedularMongo.repo.BookingRepository;
import com.nirSchedular.nirSchedularMongo.repo.UserRepository;
import com.nirSchedular.nirSchedularMongo.service.interfac.IAppointmentService;
import com.nirSchedular.nirSchedularMongo.service.interfac.IEmailService;
import com.nirSchedular.nirSchedularMongo.utils.Enums;
import com.nirSchedular.nirSchedularMongo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// @Service Marks this class as a service component in the Spring Framework, meaning it's a service that can be injected into other components.
@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository; // Handles database operations for Appointment entities (add, update, delete, find)

    @Autowired
    private BookingRepository bookingRepository; // Handles operations for the Booking entity to retrieve booking-related data

    @Autowired
    private IEmailService emailService; // Handles sending emails, such as appointment confirmations

    @Autowired
    private UserRepository userRepository;  // Handles operations for the User entity to retrieve user-related data

    @Override
    public Response bookAppointment(String userId, Appointment appointment) {
        if (appointment.getUserEmail() == null || appointment.getDate() == null || appointment.getTimeSlot() == null) {
            throw new OurException("Missing required appointment details.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OurException("User not found with ID: " + userId));

        boolean exists = appointmentRepository.existsByDateAndTimeSlotAndBookedTrue(
                appointment.getDate(), appointment.getTimeSlot());

        if (exists) {
            return Response.builder()
                    .message("An appointment is already booked for this date and time slot.")
                    .statusCode(HttpStatus.CONFLICT.value())
                    .build();
        }

        String code = Utils.generateRandomConfirmationCode(8);
        appointment.setConfirmationCode(code);
        appointment.setBooked(true);
        appointment.setUserId(userId);
        appointment.setUserEmail(user.getEmail());

        Appointment saved = appointmentRepository.save(appointment);

        String htmlContent = String.format("""
            <div dir=\"rtl\" style=\"text-align: right; font-family: Arial, sans-serif;\">
                <p>Hi %s,</p>
                <p>Your appointment has been confirmed. Here are the details:</p>
                <ul>
                    <li>Confirmation Code: %s</li>
                    <li>Date: %s</li>
                    <li>Time Slot: %s</li>
                    <li>Contact: <a href=\"tel:0526126120\">052-612-612-0</a></li>
                    <li>
                        Navigate: <a href=\"https://ul.waze.com/ul?ll=31.993925,34.764165&navigate=yes&zoom=17\" 
                                      style=\"color: #1a73e8; text-decoration: none;\" target=\"_blank\">
                                      Eliahu Eitan 3, Beit Giron - Room 107, Rishon LeZion
                        </a>
                    </li>
                </ul>
                <p>Looking forward to seeing you.</p>
                <p>Gova - Height Work Training</p>
            </div>
            """,
                user.getName(),
                code,
                appointment.getDate(),
                Enums.TimeSlot.valueOf(appointment.getTimeSlot().toUpperCase()).getHebrewLabel()
        );

        emailService.sendEmail(
                appointment.getUserEmail(),
                "Appointment Confirmation - Gova",
                htmlContent
        );

        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Appointment booked successfully.")
                .data(saved)
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
    public Response getAllAvailableAppointments() {
        Response response = new Response();

        try {
            // 1. Fetch all appointments from the database
            List<Appointment> allAppointments = appointmentRepository.findAll();

            // 2. Filter appointments that are not yet booked
            List<Appointment> availableAppointments = allAppointments.stream()  // Stream through all appointments
                    .filter(Appointment::isAvailable) // Filter appointments that are available (not booked)
                    .collect(Collectors.toList());  // Collect the filtered appointments into a list

            // 3. Set the filtered list as the data in the response
            response.setStatusCode(200);
            response.setMessage("Available appointments retrieved successfully.");
            response.setData(availableAppointments);
        } catch (Exception e) {
            // 4. Handle unexpected errors
            response.setStatusCode(500);
            response.setMessage("An error occurred while retrieving available appointments: " + e.getMessage());
            response.setData(null);
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

    @Override
    public Response getAppointmentByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        // Look up the appointment using the unique confirmation code
        Optional<Appointment> optionalAppointment = appointmentRepository.findByConfirmationCode(confirmationCode);

        if (optionalAppointment.isPresent()) {
            // Appointment found
            response.setStatusCode(200);
            response.setMessage("Appointment found.");
            response.setData(optionalAppointment.get());
        } else {
            // No appointment with this code
            response.setStatusCode(404);
            response.setMessage("No appointment found for the provided confirmation code.");
        }

        return response;
    }


}
