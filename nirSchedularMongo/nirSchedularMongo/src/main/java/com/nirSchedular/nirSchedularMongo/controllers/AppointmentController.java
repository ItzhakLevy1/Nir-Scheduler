package com.nirSchedular.nirSchedularMongo.controllers;

import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;
import com.nirSchedular.nirSchedularMongo.service.interfac.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController // This annotation indicates that this class is a Spring MVC controller, and it will handle HTTP requests.
@RequestMapping("/appointments") // Base path for all appointment-related endpoints
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;


    /**
     * Book an appointment for a user.
     * This endpoint is accessible to both ADMIN and USER roles.
     * The userId is passed as a path variable, and the appointment details are in the request body.
     */
    @PostMapping("/book/{userId}")
    public ResponseEntity<Response> bookAppointment(
            @PathVariable String userId,
            @RequestBody Appointment appointment) {

        System.out.println(">>> Booking appointment for userId: " + userId);
        System.out.println(">>> Authenticated user: " + SecurityContextHolder.getContext().getAuthentication().getName());

        Response response = appointmentService.bookAppointment(userId, appointment); // מתאם את סוג ההחזרה
        return ResponseEntity.status(response.getStatusCode()).body(response); // מחזיר Response עם קוד סטטוס מתאים
    }


    /**
     * Retrieve all appointments (for admin or general use)
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getAllAppointments() {
        Response response = appointmentService.getAllAppointments();    // Call the service to get all appointments
        return ResponseEntity.status(response.getStatusCode()).body(response);  // Return the response with the appropriate HTTP status code
    }

    /**
     * Get all available appointments (across all dates).
     * Useful for booking views or availability calendars.
     */
    @GetMapping("/all-available")
    public ResponseEntity<Response> getAllAvailableAppointments() {
        Response response = appointmentService.getAllAvailableAppointments();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Get a specific appointment by ID
     */
    @GetMapping("/by-id/{appointmentId}")
    public ResponseEntity<Response> getAppointmentById(@PathVariable("appointmentId") String appointmentId) {
        Response response = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Get available appointments for a given date
     */
    @GetMapping("/available-by-date")
    public ResponseEntity<Response> getAvailableAppointmentsByDate(
            @RequestParam(required = false) LocalDate date
    ) {
        if (date == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Date is required");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = appointmentService.getAvailableAppointmentsByDate(date);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Admin can update appointment details (in this simplified model, maybe date/time rescheduling)
     */
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<Response> updateAppointment(
            @PathVariable String appointmentId,
            @RequestBody Appointment updatedAppointment) {
        Response response = appointmentService.updateAppointment(appointmentId, updatedAppointment);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Retrieve an appointment using its confirmation code.
     * Useful for users who received a code via email to verify their booking.
     */
    @GetMapping("/get-by-confirmation-code/{code}")
    public ResponseEntity<Response> getAppointmentByConfirmationCode(@PathVariable String code) {
        Response response = appointmentService.getAppointmentByConfirmationCode(code);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Admin can delete an appointment
     */
    @DeleteMapping("/delete/{appointmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteAppointment(@PathVariable String appointmentId) {
        Response response = appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
