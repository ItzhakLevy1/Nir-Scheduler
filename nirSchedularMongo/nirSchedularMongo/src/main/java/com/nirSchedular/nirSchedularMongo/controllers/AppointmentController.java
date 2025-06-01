package com.nirSchedular.nirSchedularMongo.controllers;

import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;
import com.nirSchedular.nirSchedularMongo.service.interfac.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/appointments") // Base path for all appointment-related endpoints
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    /**
     * Admins can create a new appointment by sending an Appointment object in the request body
     */
    @PostMapping("/add")
    public ResponseEntity<Response> addNewAppointment(@RequestBody Appointment appointment) {
        Response response = appointmentService.addNewAppointment(appointment);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    /**
     * Retrieve all appointments (for admin or general use)
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getAllAppointments() {
        Response response = appointmentService.getAllAppointments();
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
     * Admin can delete an appointment
     */
    @DeleteMapping("/delete/{appointmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteAppointment(@PathVariable String appointmentId) {
        Response response = appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
